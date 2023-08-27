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
import com.bluehabit.eureka.component.AttachmentType;
import com.bluehabit.eureka.component.data.Task;
import com.bluehabit.eureka.component.data.TaskAttachment;
import com.bluehabit.eureka.component.data.TaskAttachmentRepository;
import com.bluehabit.eureka.component.data.TaskRepository;
import com.bluehabit.eureka.component.data.TaskStatusRepository;
import com.bluehabit.eureka.component.model.UploadAttachmentRequest;
import com.bluehabit.eureka.exception.GeneralErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class TaskService extends AbstractBaseService {
    private static final int maxAttachment = 3;

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskAttachmentRepository taskAttachmentRepository;

    public ResponseEntity<BaseResponse<Task>> createTemporaryTask() {
        return getAuthenticatedUser(user -> {
            return taskRepository.findPersonalTaskTemporary(user.getId())
                .map(task -> {
                    return BaseResponse.success(translate("task.create.temp.use.existing"), task);
                })
                .orElseGet(() -> {
                    final String uuid = UUID.randomUUID().toString();
                    final OffsetDateTime date = OffsetDateTime.now();
                    final Task tempTask = new Task();
                    tempTask.setId(uuid);
                    tempTask.setCreatedBy(user);
                    tempTask.setPublish(false);
                    tempTask.setCreatedAt(date);
                    tempTask.setUpdatedAt(date);

                    final Task savedTempTask = taskRepository.save(tempTask);

                    return BaseResponse.success(translate("task.create.temp.success"), savedTempTask);
                });
        }, () -> {
            throw new GeneralErrorException(HttpStatus.BAD_REQUEST.value(), translate("task.create.temp.user.not.allowed"));
        });
    }

    public ResponseEntity<BaseResponse<TaskAttachment>> uploadAttachment(
        UploadAttachmentRequest request
    ) {
        validate(request);
        return taskRepository.findById(request.taskId())
            .map(task -> {
                if (task.getAttachments().size() >= maxAttachment) {
                    throw new GeneralErrorException(HttpStatus.FORBIDDEN.value(), translate("task.attachment.upload.failed.max"));
                }
                final String idAttachment = UUID.randomUUID().toString();
                final OffsetDateTime currentDate = OffsetDateTime.now();
                final TaskAttachment taskAttachment = new TaskAttachment();
                taskAttachment.setId(idAttachment);
                taskAttachment.setMimeType(request.file().getContentType());
                taskAttachment.setTask(task);
                taskAttachment.setType(AttachmentType.IMAGE);
                taskAttachment.setCreatedAt(currentDate);
                taskAttachment.setUpdatedAt(currentDate);

                return FileUtil.saveFile(request.file(), idAttachment).map(fileName -> {
                    taskAttachment.setName(fileName);
                    final TaskAttachment savedTaskAttachment = taskAttachmentRepository.save(taskAttachment);
                    return BaseResponse.success(translate("task.attachment.upload.success"), savedTaskAttachment);
                }).orElseThrow(() -> {
                    return new GeneralErrorException(
                        HttpStatus.BAD_REQUEST.value(),
                        translate("task.attachment.upload.file.not.uploaded")
                    );
                });
            })
            .orElseThrow(() -> new GeneralErrorException(HttpStatus.BAD_REQUEST.value(), translate("task.attachment.upload.task.not.found")));
    }
}
