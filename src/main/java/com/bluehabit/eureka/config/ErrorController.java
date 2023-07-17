/*
 * Copyright © 2023 Blue Habit.
 *
 * Unauthorized copying, publishing of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 */

package com.bluehabit.eureka.config;

import com.bluehabit.eureka.common.BaseResponse;
import com.bluehabit.eureka.common.Constant;
import com.bluehabit.eureka.exception.GeneralErrorException;
import com.bluehabit.eureka.exception.UnAuthorizedException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.SignatureException;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(value = SignatureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public BaseResponse<String> signatureException(SignatureException e) {
        return BaseResponse.error(
                Constant.BKA_1000, "Authentication failed, token expired or invalid"
        );
    }

    @ExceptionHandler(value = ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public BaseResponse<String> expiredException(ExpiredJwtException e) {
        return BaseResponse.error(Constant.BKA_1001_EXPIRED,e.getMessage());
    }

    @ExceptionHandler(value = UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public BaseResponse<String> tokenNotMatchToAnyUser(UsernameNotFoundException e) {
        return BaseResponse.error(
                Constant.BKA_1002, e.getMessage()
        );
    }

    @ExceptionHandler(value = DecodingException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public BaseResponse<String> decodeException(DecodingException e) {
        return BaseResponse.error(
                Constant.BKA_1003, "Authentication failed, token expired or invalid"
        );
    }

    @ExceptionHandler(value = NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<String> nullPointer(NullPointerException e) {
        return BaseResponse.error(
                Constant.BKA_1004, e.getMessage()
        );
    }

    @ExceptionHandler(value = UnAuthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public BaseResponse<String> unAuth(UnAuthorizedException e) {
        return BaseResponse.error(
                e.getStatusCode(), e.getMessage()
        );
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<String> validation(MethodArgumentNotValidException e) {
        return BaseResponse.error(
                Constant.BKA_1006, e.getMessage()
        );
    }

    @ExceptionHandler(value = FileUploadException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<String> contentTypeFormDataNotMatch(FileUploadException e) {
        return BaseResponse.error(
                Constant.BKA_1006, e.getMessage()
        );
    }

    @ExceptionHandler(value = GeneralErrorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public BaseResponse<String> contentTypeFormDataNotMatch(GeneralErrorException e) {
        return BaseResponse.error(
                e.getStatusCode(), e.getMessage()
        );
    }

    @ExceptionHandler(value = MalformedJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public BaseResponse<String> malformed(MalformedJwtException e) {
        return BaseResponse.error(
                401, e.getMessage()
        );
    }

}