package com.bluehabit.budgetku.data.user

import com.bluehabit.budgetku.common.model.pagingResponse
import com.bluehabit.budgetku.data.apiKey.ApiKey
import com.bluehabit.budgetku.data.apiKey.ApiKeyResponse
import com.bluehabit.budgetku.data.apiKey.toResponse
import com.bluehabit.budgetku.data.permission.Permission
import com.bluehabit.budgetku.data.permission.PermissionReponse
import com.bluehabit.budgetku.data.permission.toResponse
import com.bluehabit.budgetku.data.role.RoleResponse
import com.bluehabit.budgetku.data.role.toResponse
import org.springframework.data.domain.Page
import java.time.OffsetDateTime


data class UserResponse(
    var userId: String? = null,
    var userFullName: String,
    var userCountryCode: String,
    var userDateOfBirth: String,
    var userEmail: String,
    var userAuthProvider: UserAuthProvider,
    var userStatus: UserStatus,
    var userPermission: List<PermissionReponse>,
    var userRoles: List<RoleResponse>,
    var createdAt: OffsetDateTime,
    var updatedAt: OffsetDateTime,
)

fun User.getListPermission(): List<Permission> {
    val permission = mutableListOf<Permission>()
    userRoles.forEach{ permission += it.permissions.map {result-> result } }
    return permission.toList()
}


fun User.toResponse(): UserResponse {
    val permission = mutableListOf<PermissionReponse>()

    val role = userRoles.map {
        permission += it.permissions.map { p ->
            p.toResponse()
        }
        it.toResponse()
    }

    return UserResponse(
        userId = userId,
        userFullName = userFullName,
        userEmail = userEmail,
        userAuthProvider = userAuthProvider,
        userStatus = userStatus,
        userCountryCode = userCountryCode,
        userDateOfBirth = userDateOfBirth.toString(),
        userPermission = permission,
        userRoles = role,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Page<User>.toResponse() = pagingResponse<UserResponse> {
    page = number
    currentSize = size
    items = content.map { it.toResponse() }
    totalData = totalElements
    totalPagesCount = totalPages
}