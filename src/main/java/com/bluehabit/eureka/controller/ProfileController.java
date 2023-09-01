/*
 * Copyright © 2023 Blue Habit.
 *
 * Unauthorized copying, publishing of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.bluehabit.eureka.controller;

import com.bluehabit.eureka.common.BaseResponse;
import com.bluehabit.eureka.component.data.UserCredential;
import com.bluehabit.eureka.component.model.request.ChangePasswordRequest;
import com.bluehabit.eureka.component.model.request.RequestChangePasswordRequest;
import com.bluehabit.eureka.component.model.request.UploadPhotoProfileRequest;
import com.bluehabit.eureka.component.model.response.UserStatsResponse;
import com.bluehabit.eureka.services.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/profile/")
public class ProfileController {

    @Autowired
    private UserInfoService userInfoService;

    @GetMapping(
        path = "detail",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BaseResponse<Map<String, Object>>> getProfile() {
        return userInfoService.getDetailProfile();
    }

    @GetMapping(
        path = "stats",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BaseResponse<UserStatsResponse>> getStats() {
        return userInfoService.getStatistics();
    }

    @PostMapping(
        path = "upload-profile-picture",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<BaseResponse<UserCredential>> updatePhotoProfile(
        @ModelAttribute UploadPhotoProfileRequest request
    ) {
        return userInfoService.uploadProfilePicture(request);
    }

    @PostMapping(
        path = "request-change-password",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BaseResponse<Object>> requestChangePassword(
        @ModelAttribute RequestChangePasswordRequest request
    ) {
        return userInfoService.requestChangePassword(request);
    }

    @PostMapping(
        path = "change-password",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BaseResponse<Object>> changePassword(
        @ModelAttribute ChangePasswordRequest request
    ) {
        return userInfoService.changePassword(request);
    }
}
