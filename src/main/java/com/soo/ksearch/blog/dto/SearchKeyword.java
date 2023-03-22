package com.soo.ksearch.blog.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchKeyword {
    Long searchKeywordId;
    String keyword;
    Long searchCount;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
