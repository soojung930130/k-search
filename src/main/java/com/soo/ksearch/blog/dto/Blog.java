package com.soo.ksearch.blog.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.ZonedDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Blog {
    String title;
    String contents;
    String url;
    String blogName;
    String thumbnail;
    ZonedDateTime datetime;
}
