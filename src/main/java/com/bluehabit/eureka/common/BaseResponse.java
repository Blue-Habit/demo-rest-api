/*
 * Copyright © 2023 Blue Habit.
 *
 * Unauthorized copying, publishing of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.bluehabit.eureka.common;

import jakarta.validation.ConstraintViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public final class BaseResponse<T> {
    private final int statusCode;
    private final String message;
    private final T data;

    private BaseResponse(int statusCode, String message, T data) {
        this.statusCode = statusCode;
        this.message = message;
        this.data = data;
    }

    public static <O> ResponseEntity<BaseResponse<O>> failed(int statusCode, String message) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse<>(statusCode, message, null));
    }

    public static <O> BaseResponse<O> error(int statusCode, String message) {
        return new BaseResponse<>(statusCode, message, null);
    }

    public static <O> ResponseEntity<BaseResponse<O>> success(String message, O data) {
        return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK.value(), message, data));
    }

    public static <O> ResponseEntity<BaseResponse<PageResponse<O>>> success(String message, Page<O> data) {
        return ResponseEntity.ok(new BaseResponse<>(HttpStatus.OK.value(), message, new PageResponse<>(data)));
    }

    public static Map<String, Object> validationFailed(ConstraintViolationException errors) {
            final List<Map<String, String>> constraint = errors.getConstraintViolations()
                .stream()
                .map(constraintViolation -> {
                    return Map.of(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
                }).toList();
            return Map.ofEntries(
                Map.entry("statusCode", Constant.BKA_1008),
                Map.entry("data", ""),
                Map.entry("message", "Validation"),
                Map.entry("errorField", constraint)
            );
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
