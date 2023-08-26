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
import com.bluehabit.eureka.component.data.TaskPriority;
import com.bluehabit.eureka.component.data.TaskPriorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TaskPriorityService extends AbstractBaseService {

    @Autowired
    private TaskPriorityRepository taskPriorityRepository;

    public ResponseEntity<BaseResponse<PageResponse<TaskPriority>>> getListPriority(Pageable pageable) {
        final Page<TaskPriority> taskPriorities = taskPriorityRepository.findAll(pageable);

        return BaseResponse.success(translate("auth.success"), new PageResponse<>(taskPriorities));
    }
}
