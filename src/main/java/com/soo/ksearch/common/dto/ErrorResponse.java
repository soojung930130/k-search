package com.soo.ksearch.common.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
public class ErrorResponse {
    String code;
    String message;
    String detail;
    Map<String, String> properties;
}
