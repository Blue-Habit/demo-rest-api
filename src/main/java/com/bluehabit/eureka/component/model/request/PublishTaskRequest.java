/*
 * Copyright © 2023 Blue Habit.
 *
 * Unauthorized copying, publishing of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.bluehabit.eureka.component.model.request;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record PublishTaskRequest(
    @NotBlank
    String taskId,
    @NotBlank
    String taskName,
    @NotBlank
    String taskDescription,
    String priorityId,
    String start,
    String end,
    List<SubTaskRequest> subtask
) {
}
