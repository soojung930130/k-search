package com.soo.ksearch.blog.service;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.core.api.dataset.ExpectedDataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.soo.ksearch.blog.dto.SearchKeyword;
import com.soo.ksearch.common.db.entity.SearchKeywordEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DBRider
@SpringBootTest
@ActiveProfiles("test")
class BlogSearchServiceTest {

    @Autowired
    private BlogSearchService blogSearchService;



    @Nested
    @DisplayName("키워드 검색 기록")
    @DataSet(value = {"com/soo/ksearch/blog/service/blog-search-service-test-saveSearchHistory.yml"},
            cleanBefore = true, cleanAfter = true)
    class SearchHistory {

        @Test
        @DisplayName("case1. 중복 키워드 검색")
        void case1() {
            String keyword = "ChatGPT";
            SearchKeywordEntity searchKeywordEntity = blogSearchService.saveSearchHistory(keyword);

            assertThat(searchKeywordEntity.getSearchCount()).isEqualTo(41);
        }

        @Test
        @DisplayName("case2. 신규 키워드 검색")
        @ExpectedDataSet(value = {"com/soo/ksearch/blog/service/blog-search-service-test-saveSearchHistory-expect.yml"},
                ignoreCols = {"created_at", "updated_at"})
        void case2() {
            String keyword = "soojung";
            SearchKeywordEntity searchKeywordEntity = blogSearchService.saveSearchHistory(keyword);

            assertThat(searchKeywordEntity.getSearchCount()).isEqualTo(1);
        }
    }

    @Test
    @DisplayName("인기 검색어 조회 (최대 10개)")
    @DataSet(value = {"com/soo/ksearch/blog/service/blog-search-service-test-findTop10SearchKeyword.yml"},
            cleanBefore = true, cleanAfter = true)
    void findTop10SearchKeyword() {
        List<SearchKeyword> top10SearchKeyword = blogSearchService.findTop10SearchKeyword();

        assertThat(top10SearchKeyword).isNotEmpty().hasSize(10);
    }
}