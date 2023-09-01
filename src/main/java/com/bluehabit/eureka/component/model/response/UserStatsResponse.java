/*
 * Copyright © 2023 Blue Habit.
 *
 * Unauthorized copying, publishing of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.bluehabit.eureka.component.model.response;

import java.util.List;

public record UserStatsResponse(
    StatsResponse stats,
    List<StatsActivityResponse> activity
) {
}
