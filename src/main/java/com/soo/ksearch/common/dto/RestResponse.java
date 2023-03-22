package com.soo.ksearch.common.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestResponse<T> {

    @Getter
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Page {
        int number;
        int size;

        int totalPageCount;
        long totalElementCount;
    }

    T body;
    Page page;
    boolean collection;

}
