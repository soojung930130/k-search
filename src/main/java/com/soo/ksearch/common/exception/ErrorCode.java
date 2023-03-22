package com.soo.ksearch.common.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {

    INTERNAL_SERVER_ERROR ( "0001", "서버 내부 오류",  HttpStatus.INTERNAL_SERVER_ERROR ),
    INVALID_REQUEST_WITH_REASON_MESSAGE( "0002", "유효하지 않은 요청 입니다. 사유 : ${message}",  HttpStatus.BAD_REQUEST ),
    INVALID_REQUEST( "0003", "유효하지 않은 요청 입니다",  HttpStatus.BAD_REQUEST ),
    API_SERVER_INVALID_REQUEST( "0004", "외부 검색 API 서버에 유효하지 않은 요청 입니다",  HttpStatus.BAD_REQUEST ),
    API_SERVER_ERROR( "0005", "외부 검색 API 서버 내부에 오류가 발생했습니다",  HttpStatus.INTERNAL_SERVER_ERROR ),
    ;

    final String code;
    final String message;
    final HttpStatus status;

    static final String PREFIX = "search-api";
    public String getCode() {
        return String.format("%s-%s", PREFIX, code);
    }

}
