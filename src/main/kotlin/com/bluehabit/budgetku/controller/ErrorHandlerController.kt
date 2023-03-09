/*
 * Copyright © 2023 Blue Habit.
 *
 * Unauthorized copying, publishing of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.bluehabit.budgetku.controller


import com.bluehabit.budgetku.common.exception.BadRequestException
import com.bluehabit.budgetku.common.exception.DataNotFoundException
import com.bluehabit.budgetku.common.exception.DuplicateException
import com.bluehabit.budgetku.common.exception.RestrictedException
import com.bluehabit.budgetku.common.exception.UnAuthorizedException
import com.bluehabit.budgetku.common.model.BaseResponse
import com.bluehabit.budgetku.common.model.baseResponse
import jakarta.persistence.NonUniqueResultException
import jakarta.validation.ConstraintViolationException
import org.hibernate.LazyInitializationException
import org.hibernate.exception.DataException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.http.converter.HttpMessageNotWritableException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.multipart.MaxUploadSizeExceededException
import org.springframework.web.servlet.NoHandlerFoundException
import java.text.ParseException

/**
 * Error handler controller
 * this controller for catch every error throw by defaul or custom
 * */
@RestControllerAdvice
class ErrorHandlerController {

    @ExceptionHandler(
        value = [LazyInitializationException::class]
    )
    @ResponseStatus(
        BAD_REQUEST
    )
    fun LazyInit(e:LazyInitializationException): BaseResponse<List<Any>> {
        return baseResponse {
            code = NOT_FOUND.value()
            data = listOf()
            message = e.localizedMessage
        }
    }

    @ExceptionHandler(
        value = [HttpMessageNotWritableException::class]
    )
    @ResponseStatus(
        BAD_REQUEST
    )
    fun LazyInit(e:HttpMessageNotWritableException): BaseResponse<List<Any>> {
        return baseResponse {
            code = NOT_FOUND.value()
            data = listOf()
            message = e.localizedMessage
        }
    }


    @ExceptionHandler(
        value = [NoHandlerFoundException::class]
    )
    @ResponseStatus(NOT_FOUND)
    fun resNotFound(e: NoHandlerFoundException): BaseResponse<List<Any>> {

        return baseResponse {
            code = NOT_FOUND.value()
            data = listOf()
            message = e.localizedMessage
        }
    }

    @ExceptionHandler(
        value = [IllegalArgumentException::class]
    )
    @ResponseStatus(
        BAD_REQUEST
    )
    fun illegalArgument(
        e: IllegalArgumentException
    ): BaseResponse<List<Any>> {
        return baseResponse {
            code = BAD_REQUEST.value()
            data = listOf()
            message = e.localizedMessage
        }
    }

    @ExceptionHandler(
        value = [ConstraintViolationException::class]
    )
    @ResponseStatus(
        BAD_REQUEST,
    )
    fun validationError(
        error: ConstraintViolationException
    ) = baseResponse<List<Any>> {
        code = BAD_REQUEST.value()
        data = listOf()
        message = error.localizedMessage
    }

    @ExceptionHandler(
        value = [BadRequestException::class]
    )
    @ResponseStatus(
        BAD_REQUEST
    )
    fun badRequest(
        error: BadRequestException
    ) = baseResponse<List<Any>> {
        code = BAD_REQUEST.value()
        data = listOf()
        message = error.message.orEmpty()
    }

    @ExceptionHandler(
        value = [DataNotFoundException::class]
    )
    @ResponseStatus(
        CONFLICT
    )
    fun dataNotFound(
        error: DataNotFoundException
    ) = baseResponse<List<Any>> {
        code = error.errorCode
        data = listOf()
        message = error.message.orEmpty()
    }

    @ExceptionHandler(
        value = [RestrictedException::class]
    )
    @ResponseStatus(
        CONFLICT
    )
    fun userNotAllowed(
        error: RestrictedException
    ) = baseResponse<List<Any>> {
        code = error.errorCode
        data = listOf()
        message = error.message.orEmpty()
    }

    @ExceptionHandler(
        value = [UnAuthorizedException::class]
    )
    @ResponseStatus(
        UNAUTHORIZED
    )
    fun unAuthorized(
        error: UnAuthorizedException
    ) = baseResponse<List<Any>> {
        code = BAD_REQUEST.value()
        data = listOf()
        message = error.message.orEmpty()
    }

    @ExceptionHandler(
        value = [HttpMediaTypeNotSupportedException::class]
    )
    @ResponseStatus(
        BAD_REQUEST
    )
    fun mediaTypeNotSupported(
        error: HttpMediaTypeNotSupportedException
    ) = baseResponse<List<Any>> {
        code = BAD_REQUEST.value()
        data = listOf()
        message = error.localizedMessage
    }

    @ExceptionHandler(
        value = [HttpMessageNotReadableException::class]
    )
    @ResponseStatus(
        BAD_REQUEST
    )
    fun mediaTypeJsonInvalid(
        error: HttpMessageNotReadableException
    ) = baseResponse<List<Any>> {
        code = BAD_REQUEST.value()
        data = listOf()
        message = error.localizedMessage
    }

    @ExceptionHandler(
        value = [HttpRequestMethodNotSupportedException::class]
    )
    @ResponseStatus(
        METHOD_NOT_ALLOWED
    )
    fun methodNotAllowed(
        error: HttpRequestMethodNotSupportedException
    ) = baseResponse<List<Any>> {
        code = BAD_REQUEST.value()
        data = listOf()
        message = error.localizedMessage
    }

    @ExceptionHandler(
        value = [MaxUploadSizeExceededException::class]
    )
    @ResponseStatus(
        BAD_REQUEST
    )
    fun maximumFileUpload(
        error: MaxUploadSizeExceededException
    ) = baseResponse<List<Any>> {
        code = BAD_REQUEST.value()
        data = listOf()
        message = error.localizedMessage
    }

    @ExceptionHandler(
        value = [DataIntegrityViolationException::class]
    )
    @ResponseStatus(
        BAD_REQUEST
    )
    fun sqlError(
        error: DataIntegrityViolationException
    ) = baseResponse<List<Any>> {
        code = BAD_REQUEST.value()
        data = listOf()
        message = error.mostSpecificCause.message.orEmpty()
    }

    @ExceptionHandler(
        value = [DataException::class]
    )
    @ResponseStatus(
        BAD_REQUEST
    )
    fun sqlError(
        error: DataException
    ) = baseResponse<List<Any>> {
        code = BAD_REQUEST.value()
        data = listOf()
        message = error.localizedMessage
    }

    @ExceptionHandler(
        value = [org.hibernate.exception.ConstraintViolationException::class],
    )
    @ResponseStatus(
        BAD_REQUEST
    )
    fun sqlError(
        error: org.hibernate.exception.ConstraintViolationException
    ) = baseResponse<List<Any>> {
        code = BAD_REQUEST.value()
        data = listOf()
        message = error.localizedMessage
    }

    @ExceptionHandler(
        value = [DuplicateException::class],
    )
    @ResponseStatus(
        CONFLICT
    )
    fun sqlError(
        error: DuplicateException
    ) = baseResponse<List<Any>> {
        code = error.errorCode
        data = listOf()
        message = error.message.orEmpty()
    }

    @ExceptionHandler(
        value = [NullPointerException::class]
    )
    @ResponseStatus(
        BAD_REQUEST
    )
    fun nullPointer(error: NullPointerException) =
        baseResponse<List<Any>> {
            code = BAD_REQUEST.value()
            data = listOf()
            message = error.localizedMessage
        }

    @ExceptionHandler(
        value = [ParseException::class]
    )
    @ResponseStatus(
        BAD_REQUEST
    )
    fun formatException(
        error: ParseException
    ) = baseResponse<List<Any>> {
        code = BAD_REQUEST.value()
        data = listOf()
        message = error.localizedMessage
    }

    @ExceptionHandler(
        value = [NonUniqueResultException::class]
    )
    @ResponseStatus(
        BAD_REQUEST
    )
    fun nonUniqueResult(
        error: NonUniqueResultException
    ) = baseResponse<List<Any>> {
        code = BAD_REQUEST.value()
        data = listOf()
        message = error.localizedMessage
    }
}