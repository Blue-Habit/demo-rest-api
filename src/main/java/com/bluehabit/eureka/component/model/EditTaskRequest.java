/*
 * Copyright © 2023 Blue Habit.
 *
 * Unauthorized copying, publishing of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.bluehabit.eureka.component.model;

import jakarta.validation.constraints.NotBlank;
import java.util.List;

public record EditTaskRequest(
    @NotBlank
    String taskId,
    String taskName,
    List<EditSubTaskRequest> subTasks,
    String taskDescription,
    String taskStartDate,
    String taskEndDate
) {
}
