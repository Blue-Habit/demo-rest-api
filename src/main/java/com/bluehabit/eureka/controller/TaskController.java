/*
 * Copyright © 2023 Blue Habit.
 *
 * Unauthorized copying, publishing of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.bluehabit.eureka.controller;

import com.bluehabit.eureka.common.BaseResponse;
import com.bluehabit.eureka.common.PageResponse;
import com.bluehabit.eureka.component.data.FavoriteTask;
import com.bluehabit.eureka.component.data.Task;
import com.bluehabit.eureka.component.data.TaskAttachment;
import com.bluehabit.eureka.component.data.TaskPriority;
import com.bluehabit.eureka.component.data.TaskStatus;
import com.bluehabit.eureka.component.model.EditTaskRequest;
import com.bluehabit.eureka.component.model.request.PublishTaskRequest;
import com.bluehabit.eureka.component.model.request.UploadAttachmentRequest;
import com.bluehabit.eureka.services.TaskPriorityService;
import com.bluehabit.eureka.services.TaskService;
import com.bluehabit.eureka.services.TaskStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        path = "{taskId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BaseResponse<Task>> getDetailTask(
        @PathVariable("taskId") String taskId
    ) {
        return taskService.getDetailTask(taskId);
    }

    @GetMapping(
        path = "list-starred-task",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BaseResponse<PageResponse<FavoriteTask>>> getStarredTask(
        Pageable pageable
    ) {
        return taskService.getListStarredTask(pageable);
    }

    @GetMapping(
        path = "search",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BaseResponse<List<Task>>> searchTask(
        @Param("query") String query
    ) {
        return taskService.searchTask(query);
    }

    @GetMapping(
        path = "get-list-by-date",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BaseResponse<PageResponse<Task>>> getListTask(
        @RequestParam("from") String fromDate,
        @RequestParam("to") String toDate,
        Pageable pageable
    ) {
        return taskService.getListTaskByDate(fromDate, toDate, pageable);
    }

    @GetMapping(
        path = "list-task",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BaseResponse<PageResponse<Task>>> getListTask(
        Pageable pageable
    ) {
        return taskService.getListTask(pageable);
    }

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

    @DeleteMapping(
        path = "delete-attachment/{attachmentId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BaseResponse<TaskAttachment>> deleteAttachment(
        @PathVariable("attachmentId") String attachmentId
    ) {
        return taskService.deleteAttachment(attachmentId);
    }

    @PostMapping(
        path = "publish",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BaseResponse<Task>> publishTask(
        @RequestBody PublishTaskRequest request
    ) {
        return taskService.publishTask(request);
    }

    @GetMapping(
        path = "task-list-by-status/{statusId}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BaseResponse<PageResponse<Task>>> getListTaskByStatus(
        @PathVariable("statusId") String statusId,
        Pageable pageable
    ) {
        return taskService.getListTaskByStatus(statusId, pageable);
    }

    @PostMapping(
        path = "edit",
        produces = MediaType.APPLICATION_JSON_VALUE,
        consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<BaseResponse<Task>> editTask(
        @RequestBody EditTaskRequest request
        ) {
        return taskService.editTask(request);
    }

}
