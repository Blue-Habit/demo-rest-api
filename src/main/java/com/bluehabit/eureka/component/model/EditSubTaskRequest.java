/*
 * Copyright © 2023 Blue Habit.
 *
 * Unauthorized copying, publishing of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.bluehabit.eureka.component.model;

import jakarta.validation.constraints.NotBlank;

public record EditSubTaskRequest(
    @NotBlank
    String subTaskId,
    String subTaskName,
    boolean done
) {
}
