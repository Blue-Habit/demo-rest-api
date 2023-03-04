/*
 * Copyright © 2023 Blue Habit.
 *
 * Unauthorized copying, publishing of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.bluehabit.budgetku.config.apiKeyMiddleware

import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter

class ApiKeyAuthFilter(
    private val headerName:String
) : AbstractPreAuthenticatedProcessingFilter() {
    override fun getPreAuthenticatedPrincipal(request: HttpServletRequest?): Any {
        return request?.getHeader(headerName) ?: ""
    }

    override fun getPreAuthenticatedCredentials(request: HttpServletRequest?): Any? {
       return null
    }


}