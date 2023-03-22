package com.soo.ksearch.blog.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(title = "인기 검색어 응답")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PopularKeywordResponse {
    @Schema(description = "키워드")
    String keyword;
    @Schema(description = "검색 횟수")
    Long searchCount;
}
