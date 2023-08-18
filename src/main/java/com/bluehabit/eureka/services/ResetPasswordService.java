
/*
 * Copyright © 2023 Blue Habit.
 *
 * Unauthorized copying, publishing of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.bluehabit.eureka.services;

import com.bluehabit.eureka.common.AbstractBaseService;
import com.bluehabit.eureka.common.BaseResponse;
import com.bluehabit.eureka.common.Constant;
import com.bluehabit.eureka.common.MailUtil;
import com.bluehabit.eureka.common.TokenGenerator;
import com.bluehabit.eureka.component.user.UserCredential;
import com.bluehabit.eureka.component.user.UserCredentialRepository;
import com.bluehabit.eureka.component.user.UserVerification;
import com.bluehabit.eureka.component.user.UserVerificationRepository;
import com.bluehabit.eureka.component.user.model.RequestResetPasswordRequest;
import com.bluehabit.eureka.component.user.model.ResetPasswordRequest;
import com.bluehabit.eureka.component.user.verification.VerificationType;
import com.bluehabit.eureka.exception.GeneralErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ResetPasswordService extends AbstractBaseService {
    @Autowired
    private UserVerificationRepository userVerificationRepository;
    @Autowired
    private UserCredentialRepository userCredentialRepository;

    @Autowired
    private MailUtil mailUtil;
    @Autowired
    private TokenGenerator tokenGenerator;

    public ResponseEntity<BaseResponse<Object>> resetPassword(String token, ResetPasswordRequest request) {
        validate(request);
        final Optional<UserVerification> userVerification = userVerificationRepository.findByToken(token);

        if (userVerification.isEmpty()) {
            throw new GeneralErrorException(HttpStatus.NOT_FOUND.value(), translate("auth.token.invalid"));
        }
        final UserVerification userVerified = userVerification.get();
        final UserCredential userCredential = userVerified.getUser();
        userCredential.setPassword(request.newPassword());
        userCredentialRepository.save(userCredential);

        final boolean isMailed = mailUtil.sendEmail(
            userCredential.getEmail(),
            translate("auth.reset_password.subject"),
            "reset-password-notification",
            Map.of("user", "user name"),
            (success) -> success
        );

        if (!isMailed) {
            throw new GeneralErrorException(HttpStatus.BAD_REQUEST.value(), translate("auth.invalid"));
        }

        return BaseResponse.success(translate("auth.success"), new HashMap<>());
    }

    public ResponseEntity<BaseResponse<Object>> requestResetPassword(RequestResetPasswordRequest request) {
        validate(request);
        return userCredentialRepository.findByEmail(request.email())
            .map(userCredential -> {
                final UserVerification userVerification = new UserVerification();
                userVerification.setUser(userCredential);
                userVerification.setType(VerificationType.RESET);
                userVerification.setToken(TokenGenerator.generateToken());
                userVerificationRepository.save(userVerification);
                final boolean isMailed = mailUtil.sendEmail(
                    userCredential.getEmail(),
                    translate("auth.request.reset.password.subject"),
                    Constant.RESET_PASSWORD_REQUEST_FOLDER,
                    Map.of("link", String.format("https://gawean.com/%s", userVerification.getToken())),
                    (success) -> success
                );
                if (!isMailed) {
                    throw new GeneralErrorException(HttpStatus.BAD_REQUEST.value(), translate("auth.user.not.exist"));
                }
                return BaseResponse.success(translate("auth.success"), new Object());
            })
            .orElseThrow(() -> new GeneralErrorException(HttpStatus.NOT_FOUND.value(), translate("auth.user.not.exist")));
    }
}