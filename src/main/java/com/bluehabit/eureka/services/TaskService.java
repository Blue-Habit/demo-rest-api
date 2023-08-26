/*
 * Copyright © 2023 Blue Habit.
 *
 * Unauthorized copying, publishing of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.bluehabit.eureka.services;

import com.bluehabit.eureka.common.AbstractBaseService;
import com.bluehabit.eureka.common.BaseResponse;
import com.bluehabit.eureka.component.data.Task;
import com.bluehabit.eureka.component.data.TaskRepository;
import com.bluehabit.eureka.component.data.TaskStatusRepository;
import com.bluehabit.eureka.exception.GeneralErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class TaskService extends AbstractBaseService {

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    @Autowired
    private TaskRepository taskRepository;

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
}