package com.soo.ksearch.common.exception;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import java.util.LinkedHashMap;
import java.util.Map;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchApiExceptionBuilder {

    final Map<String, String> properties;
    final ErrorCode errorCode;
    Throwable cause;
    HttpStatus status;

    private SearchApiExceptionBuilder(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.status = errorCode.getStatus();
        this.properties = new LinkedHashMap<>();
    }

    public static SearchApiExceptionBuilder builder(ErrorCode errorCode) {
        return new SearchApiExceptionBuilder(errorCode);
    }

    public SearchApiException build() {
        return new SearchApiException(errorCode, cause, status, properties);
    }

    public SearchApiExceptionBuilder properties(String key, String value) {

        this.properties.put(key, value);
        return this;
    }

    public SearchApiExceptionBuilder properties(Map<String, String> properties) {
        this.properties.putAll(properties);
        return this;
    }

    public SearchApiExceptionBuilder cause(Throwable cause) {

        this.cause = cause;
        return this;
    }

}
