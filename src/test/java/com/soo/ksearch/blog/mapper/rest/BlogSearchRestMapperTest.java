package com.soo.ksearch.blog.mapper.rest;

import com.soo.ksearch.blog.dto.SearchKeyword;
import com.soo.ksearch.blog.dto.response.PopularKeywordResponse;
import com.soo.ksearch.blog.dto.response.SearchBlogResponse;
import com.soo.ksearch.common.db.entity.SearchKeywordEntity;
import com.soo.ksearch.rest.client.kakao.dto.response.KakaoOpenApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("test")
@SpringBootTest
class BlogSearchRestMapperTest {
    @Autowired
    private BlogSearchRestMapper blogSearchRestMapper;

    @Test
    void toSearchBlogsResponseRestDto() {
        KakaoOpenApiResponse.Documents documents = KakaoOpenApiResponse.Documents.builder()
                .title("title")
                .contents("contents")
                .url("url")
                .blogName("blogName")
                .thumbnail("thumbnail")
                .datetime(ZonedDateTime.now())
                .build();

        SearchBlogResponse searchBlogResponse = blogSearchRestMapper.toSearchBlogsResponseRestDto(documents);

        assertThat(searchBlogResponse.getTitle()).isEqualTo(documents.getTitle());
        assertThat(searchBlogResponse.getContents()).isEqualTo(documents.getContents());
        assertThat(searchBlogResponse.getUrl()).isEqualTo(documents.getUrl());
        assertThat(searchBlogResponse.getBlogName()).isEqualTo(documents.getBlogName());
        assertThat(searchBlogResponse.getThumbnail()).isEqualTo(documents.getThumbnail());
        assertThat(searchBlogResponse.getDatetime()).isEqualTo(documents.getDatetime());
    }

    @Test
    void toSearchKeywordDto() {
        SearchKeywordEntity searchKeywordEntity = SearchKeywordEntity.builder()
                .searchKeywordId(1L)
                .keyword("keyword")
                .searchCount(1L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        SearchKeyword searchKeyword = blogSearchRestMapper.toDto(searchKeywordEntity);

        assertThat(searchKeyword.getSearchKeywordId()).isEqualTo(searchKeywordEntity.getSearchKeywordId());
        assertThat(searchKeyword.getKeyword()).isEqualTo(searchKeywordEntity.getKeyword());
        assertThat(searchKeyword.getSearchCount()).isEqualTo(searchKeywordEntity.getSearchCount());
        assertThat(searchKeyword.getCreatedAt()).isEqualTo(searchKeywordEntity.getCreatedAt());
        assertThat(searchKeyword.getUpdatedAt()).isEqualTo(searchKeywordEntity.getUpdatedAt());
    }

    @Test
    void toPopularKeywordResponseRestDto() {
        SearchKeyword searchKeyword = SearchKeyword.builder()
                .searchKeywordId(1L)
                .keyword("keyword")
                .searchCount(1L)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        PopularKeywordResponse popularKeywordResponse = blogSearchRestMapper.toPopularKeywordResponseRestDto(searchKeyword);

        assertThat(popularKeywordResponse.getKeyword()).isEqualTo(searchKeyword.getKeyword());
        assertThat(popularKeywordResponse.getSearchCount()).isEqualTo(searchKeyword.getSearchCount());
    }
}