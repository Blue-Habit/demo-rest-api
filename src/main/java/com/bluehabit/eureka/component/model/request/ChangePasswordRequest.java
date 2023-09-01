/*
 * Copyright © 2023 Blue Habit.
 *
 * Unauthorized copying, publishing of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.bluehabit.eureka.component.model.request;

import jakarta.validation.constraints.NotBlank;

public record ChangePasswordRequest(@NotBlank String password) {
}
