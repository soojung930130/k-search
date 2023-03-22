package com.soo.ksearch.rest.client.kakao.dto.code;

import com.google.common.base.Enums;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum SortType {
    accuracy("정확도순"),
    recency("최신순")
    ;
    final String label;

    public static SortType of(String codeName) {
        return Enums.getIfPresent(SortType.class, codeName).or(accuracy);
    }
}
