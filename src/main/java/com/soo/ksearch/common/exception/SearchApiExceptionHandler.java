package com.soo.ksearch.common.exception;

import com.soo.ksearch.common.dto.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class SearchApiExceptionHandler {

    @ExceptionHandler(value = BindException.class)
    public ErrorResponse handleBindException(BindException e,
                                             HttpServletRequest request,
                                             HttpServletResponse response) {

        return handleBindingResult(e, e.getBindingResult(), request, response);
    }

    private ErrorResponse handleBindingResult(Exception e,
                                              BindingResult bindingResult,
                                              HttpServletRequest request,
                                              HttpServletResponse response) {

        StringBuilder stringBuilder = new StringBuilder();

        SearchApiExceptionBuilder builder =
                SearchApiExceptionBuilder.builder(ErrorCode.INVALID_REQUEST_WITH_REASON_MESSAGE)
                        .cause(e);

        bindingResult.getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = String.format("[%s] %s", fieldName, error.getDefaultMessage());
            builder.properties(fieldName, errorMessage);

            stringBuilder.append(errorMessage);
            stringBuilder.append("\n");
        });

        builder.properties("message", stringBuilder.toString());
        SearchApiException searchApiException = builder.build();
        ErrorResponse errorResponse = getErrorResponse(searchApiException);

        this.writeLog(request, LogLevel.WARN, searchApiException, errorResponse);
        response.setStatus(searchApiException.getStatus().value());
        return errorResponse;
    }

    private ErrorResponse getErrorResponse(SearchApiException e) {
        return ErrorResponse.builder()
                .code(e.getErrorCode())
                .message(e.getMessage())
                .properties(e.getProperties())
                .build();
    }

    private void writeLog(HttpServletRequest request, LogLevel logLevel, Exception e,
                          ErrorResponse errorResponse) {

        String message =
                "Error in process. [Request] : {} {} \n (errorCode : {}, properties : {}, cause : {})";
        Object[] args = {
                request.getMethod(),
                request.getRequestURI(),
                errorResponse.getCode(),
                errorResponse.getProperties(),
                e.getMessage(),
                e
        };

        if (logLevel == LogLevel.ERROR) {
            log.error(message, args);
        } else {
            log.warn(message, args);
        }
    }

    @ExceptionHandler(value = Exception.class)
    public ErrorResponse handleException(Exception e,
                                         HttpServletRequest request,
                                         HttpServletResponse response) {

        SearchApiException searchApiException =
                SearchApiExceptionBuilder.builder(ErrorCode.INTERNAL_SERVER_ERROR)
                        .cause(e)
                        .build();

        ErrorResponse errorResponse = this.getErrorResponse(searchApiException);
        this.writeLog(request, LogLevel.ERROR, searchApiException, errorResponse);
        response.setStatus(searchApiException.getStatus().value());

        return errorResponse;
    }

    @ExceptionHandler(value = SearchApiException.class)
    public ErrorResponse handleSearchApiException(SearchApiException e,
                                                  HttpServletRequest request,
                                                  HttpServletResponse response) {

        ErrorResponse errorResponse = this.getErrorResponse(e);

        this.writeLog(request,
                e.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR ? LogLevel.ERROR : LogLevel.WARN, e,
                errorResponse);
        response.setStatus(e.getStatus().value());

        return errorResponse;
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ErrorResponse handleConstraintViolationException(ConstraintViolationException e,
                                                            HttpServletRequest request,
                                                            HttpServletResponse response) {

        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        ConstraintViolation<?> first = constraintViolations.stream().findFirst().orElse(null);

        String message = null;
        if (first != null) {
            message = first.getMessage();
        }

        SearchApiExceptionBuilder builder = SearchApiExceptionBuilder.builder(ErrorCode.INVALID_REQUEST_WITH_REASON_MESSAGE)
                .properties("message", message)
                .cause(e);
        constraintViolations.forEach(cv -> builder.properties(cv.getPropertyPath().toString(), cv.getMessage()));

        SearchApiException searchApiException = builder.build();
        ErrorResponse errorResponse = this.getErrorResponse(searchApiException);

        this.writeLog(request,
                LogLevel.WARN, searchApiException,
                errorResponse);
        response.setStatus(searchApiException.getStatus().value());

        return errorResponse;

    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class})
    public ErrorResponse handleHttpMethodNotReadableException(Exception e, HttpServletRequest request,
                                                              HttpServletResponse response) {

        SearchApiExceptionBuilder builder;

        if (ExceptionUtils.getRootCause(e) instanceof SearchApiException) {
            SearchApiException searchApiException = (SearchApiException) ExceptionUtils.getRootCause(e);
            builder = SearchApiExceptionBuilder.builder(ErrorCode.INVALID_REQUEST_WITH_REASON_MESSAGE)
                    .properties(searchApiException.getProperties());

        } else {
            builder = SearchApiExceptionBuilder.builder(ErrorCode.INVALID_REQUEST).cause(e);
        }

        SearchApiException searchApiException = builder.build();
        ErrorResponse errorResponse = this.getErrorResponse(searchApiException);
        this.writeLog(request, LogLevel.WARN, searchApiException, errorResponse);
        response.setStatus(searchApiException.getStatus().value());

        return errorResponse;
    }
}
