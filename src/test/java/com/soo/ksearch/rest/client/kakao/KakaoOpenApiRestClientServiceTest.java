package com.soo.ksearch.rest.client.kakao;

import com.soo.ksearch.rest.client.kakao.dto.code.SortType;
import com.soo.ksearch.rest.client.kakao.dto.response.KakaoOpenApiResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Disabled
class KakaoOpenApiRestClientServiceTest {

    @Autowired
    private KakaoOpenApiRestClientService kakaoOpenApiRestClientService;

    @DisplayName("블로그 검색하기")
    @Test
    void searchBlog() {
        KakaoOpenApiResponse kakaoOpenApiResponse = kakaoOpenApiRestClientService.searchBlog("ChatGPT", SortType.recency, 1, 5);
        assertThat(kakaoOpenApiResponse).isNotNull();
    }
}