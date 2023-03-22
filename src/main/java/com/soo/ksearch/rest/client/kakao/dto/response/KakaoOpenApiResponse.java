package com.soo.ksearch.rest.client.kakao.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.ZonedDateTime;
import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class KakaoOpenApiResponse {
    Meta meta;
    ArrayList<Documents> documents;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @ToString
    public static class Meta {
        @JsonProperty("total_count")
        int totalCount;
        @JsonProperty("pageable_count")
        int pageableCount;
        @JsonProperty("is_end")
        boolean isEnd;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @ToString
    public static class Documents {
        String title;
        String contents;
        String url;
        @JsonProperty("blogname")
        String blogName;
        String thumbnail;
        ZonedDateTime datetime;
    }
}
