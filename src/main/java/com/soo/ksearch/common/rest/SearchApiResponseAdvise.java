package com.soo.ksearch.common.rest;

import com.soo.ksearch.common.dto.RestResponseFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class SearchApiResponseAdvise implements ResponseBodyAdvice<Object> {

    private final RestResponseFactory responseFactory = RestResponseFactory.getInstance();

    @Override
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter methodParameter,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
                                  ServerHttpResponse response) {

        if (!request.getURI().getPath().startsWith("/search-api")) {
            return body;
        }

        if (methodParameter.hasMethodAnnotation(ExceptionHandler.class)) {
            return responseFactory.create(response, body);
        }

        return responseFactory.create(response, body, HttpStatus.OK);
    }
}