package com.soo.ksearch.blog.controller;

import com.soo.ksearch.blog.dto.SearchKeyword;
import com.soo.ksearch.blog.service.BlogSearchService;
import com.soo.ksearch.rest.client.kakao.KakaoOpenApiRestClientService;
import com.soo.ksearch.rest.client.kakao.dto.code.SortType;
import com.soo.ksearch.rest.client.kakao.dto.response.KakaoOpenApiResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
class SearchBlogControllerTest {

    @MockBean
    private BlogSearchService blogSearchService;

    @MockBean
    private KakaoOpenApiRestClientService kakaoOpenApiRestClientService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @DisplayName("블로그 검색")
    void searchBlogs() throws Exception {
        when(kakaoOpenApiRestClientService.searchBlog("ChatGPT", SortType.accuracy, 1, 5))
                .thenReturn(KakaoOpenApiResponse.builder()
                        .meta(KakaoOpenApiResponse.Meta.builder()
                                .pageableCount(1)
                                .totalCount(1)
                                .isEnd(true)
                                .build())
                        .documents(Lists.newArrayList(KakaoOpenApiResponse.Documents.builder()
                                .title("title")
                                .contents("contents")
                                .url("url")
                                .blogName("blogName")
                                .thumbnail("thumbnail")
                                .datetime(ZonedDateTime.now())
                                .build()))
                        .build());

        mockMvc.perform(get("/search-api/v1/blog/blogs")
                        .param("pageNumber", "1")
                        .param("pageSize", "5")
                        .param("keyword", "ChatGPT")
                        .param("sortType", "accuracy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("body[0].title").value("title"))
                .andExpect(jsonPath("body[0].contents").value("contents"))
                .andExpect(jsonPath("body[0].url").value("url"))
                .andExpect(jsonPath("body[0].blogName").value("blogName"))
                .andExpect(jsonPath("body[0].thumbnail").value("thumbnail"));
    }

    @Test
    @DisplayName("인기 검색어 목록")
    void searchTop10SearchKeyword() throws Exception {
        when(blogSearchService.findTop10SearchKeyword())
                .thenReturn(Lists.newArrayList(SearchKeyword.builder()
                        .searchKeywordId(1L)
                        .searchCount(1L)
                        .keyword("ChatGPT")
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()));

        mockMvc.perform(get("/search-api/v1/blog/favorite-keywords"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("body[0].keyword").value("ChatGPT"))
                .andExpect(jsonPath("body[0].searchCount").value(1));
    }
}