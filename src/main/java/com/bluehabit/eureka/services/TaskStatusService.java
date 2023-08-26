/*
 * Copyright © 2023 Blue Habit.
 *
 * Unauthorized copying, publishing of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.bluehabit.eureka.services;

import com.bluehabit.eureka.common.AbstractBaseService;
import com.bluehabit.eureka.common.BaseResponse;
import com.bluehabit.eureka.common.PageResponse;
import com.bluehabit.eureka.component.data.TaskStatus;
import com.bluehabit.eureka.component.data.TaskStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TaskStatusService extends AbstractBaseService {

    @Autowired
    private TaskStatusRepository taskStatusRepository;

    public ResponseEntity<BaseResponse<PageResponse<TaskStatus>>> getListStatus(Pageable pageable) {
        final Page<TaskStatus> taskPriorities = taskStatusRepository.findAll(pageable);
        return BaseResponse.success(translate("auth.success"), taskPriorities);
    }
}
