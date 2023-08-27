/*
 * Copyright © 2023 Blue Habit.
 *
 * Unauthorized copying, publishing of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.bluehabit.eureka.controller;

import com.bluehabit.eureka.common.BaseResponse;
import com.bluehabit.eureka.common.PageResponse;
import com.bluehabit.eureka.component.data.Task;
import com.bluehabit.eureka.component.data.TaskAttachment;
import com.bluehabit.eureka.component.data.TaskPriority;
import com.bluehabit.eureka.component.data.TaskStatus;
import com.bluehabit.eureka.component.model.UploadAttachmentRequest;
import com.bluehabit.eureka.services.TaskPriorityService;
import com.bluehabit.eureka.services.TaskService;
import com.bluehabit.eureka.services.TaskStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/task/")
public class TaskController {
    @Autowired
    private TaskPriorityService taskPriorityService;

    @Autowired
    private TaskStatusService taskStatusService;

    @Autowired
    private TaskService taskService;

    @GetMapping(
        path = "priority-list",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BaseResponse<PageResponse<TaskPriority>>> getListPriority(
        Pageable pageable
    ) {
        return taskPriorityService.getListPriority(pageable);
    }

    @GetMapping(
        path = "status-list",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BaseResponse<PageResponse<TaskStatus>>> getListStatus(
        Pageable pageable
    ) {
        return taskStatusService.getListStatus(pageable);
    }

    @PostMapping(
        path = "create-temporary-task",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BaseResponse<Task>> createTemporaryTask() {
        return taskService.createTemporaryTask();
    }

    @PostMapping(
        path = "upload-attachment",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<BaseResponse<TaskAttachment>> uploadAttachment(
        @ModelAttribute UploadAttachmentRequest request
    ) {
        return taskService.uploadAttachment(request);
    }
}
