/*
 * Copyright © 2023 Blue Habit.
 *
 * Unauthorized copying, publishing of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.bluehabit.eureka.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;

public abstract class AbstractBaseService {
    @Autowired
    private ResourceBundleMessageSource i81n;

    protected String translate(String key, String... params) {
        return i81n.getMessage(key, params, LocaleContextHolder.getLocale());
    }
}
