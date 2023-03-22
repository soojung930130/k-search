package com.soo.ksearch.blog.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.ZonedDateTime;

@Schema(title = "블로그 검색 응답")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchBlogResponse {

    @Schema(description = "블로그 글 제목")
    String title;
    @Schema(description = "블로그 글 요약")
    String contents;
    @Schema(description = "블로그 글 URL")
    String url;
    @Schema(description = "블로그의 이름")
    String blogName;
    @Schema(description = "대표 미리보기 이미지 URL (미리보기 크기 및 화질은 변경될 수 있음)")
    String thumbnail;
    @Schema(description = "블로그 글 작성시간, ISO 8601")
    ZonedDateTime datetime;
}
