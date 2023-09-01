/*
 * Copyright © 2023 Blue Habit.
 *
 * Unauthorized copying, publishing of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.bluehabit.eureka.services;

import com.bluehabit.eureka.common.AbstractBaseService;
import com.bluehabit.eureka.common.BaseResponse;
import com.bluehabit.eureka.common.FileUtil;
import com.bluehabit.eureka.component.data.Task;
import com.bluehabit.eureka.component.data.TaskRepository;
import com.bluehabit.eureka.component.data.UserCredential;
import com.bluehabit.eureka.component.data.UserCredentialRepository;
import com.bluehabit.eureka.component.data.UserProfile;
import com.bluehabit.eureka.component.data.UserProfileRepository;
import com.bluehabit.eureka.component.model.UserInfoDetails;
import com.bluehabit.eureka.component.model.request.ChangePasswordRequest;
import com.bluehabit.eureka.component.model.request.RequestChangePasswordRequest;
import com.bluehabit.eureka.component.model.request.UploadPhotoProfileRequest;
import com.bluehabit.eureka.component.model.response.StatsActivityResponse;
import com.bluehabit.eureka.component.model.response.StatsResponse;
import com.bluehabit.eureka.component.model.response.UserStatsResponse;
import com.bluehabit.eureka.exception.GeneralErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserInfoService extends AbstractBaseService implements UserDetailsService {
    private static final String keyPhotoProfile = "photoProfile";
    @Autowired
    private UserCredentialRepository userCredentialRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userCredentialRepository.findByEmail(username)
            .map(UserInfoDetails::new)
            .orElseThrow(() -> new UsernameNotFoundException("user not found " + username));

    }

    public ResponseEntity<BaseResponse<Map<String, Object>>> getDetailProfile() {
        return getAuthenticatedUser(userCredential -> {
            return BaseResponse.success(translate(""), userCredential.toResponse());
        });
    }

    public ResponseEntity<BaseResponse<UserCredential>> uploadProfilePicture(
        UploadPhotoProfileRequest request
    ) {
        validate(request);
        return getAuthenticatedUser(userCredential -> {
            final String uuid = UUID.randomUUID().toString();
            final UserProfile userProfile = new UserProfile();
            final OffsetDateTime currentDate = OffsetDateTime.now();
            userProfile.setId(uuid);
            userProfile.setKey(keyPhotoProfile);
            userProfile.setUserId(userCredential.getId());
            userProfile.setCreatedAt(currentDate);
            userProfile.setUpdatedAt(currentDate);
            return FileUtil.saveProfilePicture(
                    request.profilePicture(),
                    uuid
                ).map(name -> {
                    userProfile.setValue(name);
                    final UserProfile savedProfile = userProfileRepository.save(userProfile);

                    final List<UserProfile> profileList = new java.util.ArrayList<>(userCredential.getUserInfo().stream().toList());
                    profileList.add(userProfile);
                    userCredential.setUserInfo(profileList);
                    return BaseResponse.success(translate(""), userCredential);
                })
                .orElseThrow(() -> new GeneralErrorException(HttpStatus.BAD_REQUEST.value(), translate("")));

        });
    }

    public ResponseEntity<BaseResponse<Object>> requestChangePassword(
        RequestChangePasswordRequest request
    ) {
        validate(request);
        return getAuthenticatedUser(
            userCredential -> {
                if (!encoder.matches(request.password(), userCredential.getPassword())) {
                    throw new GeneralErrorException(HttpStatus.BAD_REQUEST.value(), translate(""));
                }
                return BaseResponse.success(translate(""), Map.of());
            }
        );
    }

    public ResponseEntity<BaseResponse<Object>> changePassword(
        ChangePasswordRequest request
    ) {
        validate(request);
        return getAuthenticatedUser(
            userCredential -> {
                userCredential.setPassword(encoder.encode(request.password()));
                userCredential.setUpdatedAt(OffsetDateTime.now());
                userCredentialRepository.save(userCredential);
                return BaseResponse.success(translate(""), Map.of());
            }
        );
    }

    public ResponseEntity<BaseResponse<UserStatsResponse>> getStatistics() {
        return getAuthenticatedUser(
            userCredential -> {
                final OffsetDateTime currentDateTime = OffsetDateTime.now();
                final TemporalField field = WeekFields.of(LocaleContextHolder.getLocale()).dayOfWeek();
                final OffsetDateTime startDayOfWeek = currentDateTime.with(field, 1);
                final OffsetDateTime endDayOfWeek = startDayOfWeek.plusDays(7);

                final List<Task> tasks = taskRepository.getStatistic(
                    userCredential.getId(),
                    startDayOfWeek,
                    endDayOfWeek
                );

                final List<StatsActivityResponse> activity = tasks.stream()
                    .collect(Collectors.groupingBy(task -> task.getCreatedAt().toLocalDate()))
                    .entrySet().stream().map(
                        value -> {
                            return new StatsActivityResponse(
                                value.getKey(),
                                value.getValue().size()
                            );
                        }
                    ).toList();

                return BaseResponse.success(translate(""), new UserStatsResponse(
                    new StatsResponse(0, 0, 0),
                    activity
                ));
            }
        );
    }

}
