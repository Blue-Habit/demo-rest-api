/*
 * Copyright © 2023 Blue Habit.
 *
 * Unauthorized copying, publishing of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.bluehabit.eureka.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public final class PageResponse<T> {
    private final int page;
    private final int size;
    private final long totalElements;
    private final long totalPages;
    private final List<T> items;

    public PageResponse(
        Page<T> data
    ) {
        this.page = data.getNumber();
        this.size = data.getSize();
        this.totalElements = data.getTotalElements();
        this.totalPages = data.getTotalPages();
        this.items = data.stream().toList();
    }
}
