package com.soo.ksearch.common.dto;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpResponse;

import java.util.Collection;
import java.util.List;

public class RestResponseFactory {

    private static class LazyHolder {

        private static final RestResponseFactory INSTANCE = new RestResponseFactory();
    }

    public static RestResponseFactory getInstance() {

        return LazyHolder.INSTANCE;
    }

    private RestResponseFactory() {

        super();
    }

    public <T extends RestResponse<?>> T create(ServerHttpResponse response,
                                                Object body) {

        return this.create(response, body, null);
    }

    public <T extends RestResponse<?>> T create(ServerHttpResponse response,
                                                Object body,
                                                HttpStatus status) {


        RestResponse<Object> responseBody = new RestResponse<>();
        responseBody.setBody(body);

        if (body instanceof Page) {
            Page p = (Page) body;

            List<?> list = p.getContent();

            responseBody.setBody(list);
            responseBody.setCollection(true);
            responseBody.setPage(RestResponse.Page.builder()
                    .number(p.getNumber())
                    .size(p.getSize())
                    .totalElementCount(p.getTotalElements())
                    .totalPageCount(p.getTotalPages())
                    .build());

        } else if (body instanceof Collection) {
            responseBody.setCollection(true);
        }

        if (status != null) {
            response.setStatusCode(status);
        }

        return (T) responseBody;
    }

}
