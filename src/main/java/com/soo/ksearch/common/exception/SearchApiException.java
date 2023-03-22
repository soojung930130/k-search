package com.soo.ksearch.common.exception;

import lombok.Getter;
import org.apache.commons.collections4.MapUtils;
import org.springframework.http.HttpStatus;

import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public class SearchApiException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Map<String, String> properties = new LinkedHashMap<>();
    private final HttpStatus status;

    public SearchApiException(ErrorCode errorCode, Throwable cause, HttpStatus status, Map<String, String> properties) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
        this.status = status;
        if (properties != null) this.properties.putAll(properties);
    }

    public Map<String, String> getProperties() {
        return MapUtils.unmodifiableMap(properties);
    }

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }

    public String getErrorCode() {
        return errorCode.getCode();
    }
}

