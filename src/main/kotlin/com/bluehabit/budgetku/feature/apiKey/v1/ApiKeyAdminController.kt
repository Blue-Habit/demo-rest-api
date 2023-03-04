/*
 * Copyright © 2023 Blue Habit.
 *
 * Unauthorized copying, publishing of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.bluehabit.budgetku.feature.apiKey.v1

import com.bluehabit.budgetku.data.apiKey.ApiKeyService
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(
    value = ["/api/v1/admin"]
)
class ApiKeyAdminController(
    private val apiKeyService: ApiKeyService
) {
    @GetMapping(
        value = ["/api-keys"],
        produces = ["application/json"]
    )
    suspend fun getAllApiKeys(pageable: Pageable) =
        apiKeyService.getAllApiKeys(pageable)


    @PostMapping(
        value = ["/api-key"],
        produces = ["application/json"]
    )
    fun generateToken(
    )=apiKeyService.generateApiKey()

    @DeleteMapping(
        value = ["/api-key/{api_key_id}"],
        produces = ["application/json"]
    )
    fun deleteCredential(
        @PathVariable("api_key_id") apiKeyId:String
    )=apiKeyService.deleteApiKey(apiKeyId)
}