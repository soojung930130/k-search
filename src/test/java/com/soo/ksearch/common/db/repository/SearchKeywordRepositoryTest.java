package com.soo.ksearch.common.db.repository;

import com.github.database.rider.core.api.dataset.DataSet;
import com.github.database.rider.junit5.api.DBRider;
import com.soo.ksearch.common.db.entity.SearchKeywordEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DBRider
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class SearchKeywordRepositoryTest {

    @Autowired
    private SearchKeywordRepository searchKeywordRepository;

    @Nested
    @DisplayName("키워드 검색")
    @DataSet(value = {"com/soo/ksearch/common/db/repository/search-keyword-repository-test-findByKeyword.yml"},
            cleanBefore = true, cleanAfter = true)
    class FindByKeyword {
        @Test
        @DisplayName("case1. 키워드 조회")
        void case1() {
            String keyword = "ChatGPT";
            Optional<SearchKeywordEntity> searchKeywordEntity = searchKeywordRepository.findByKeyword(keyword);

            assertThat(searchKeywordEntity.isPresent()).isTrue();
            SearchKeywordEntity searchKeyword = searchKeywordEntity.get();
            assertThat(searchKeyword.getKeyword()).isEqualTo(keyword);
        }

        @Test
        @DisplayName("case2. 신규 키워드 저장")
        void case2() {
            String keyword = "soojung";
            SearchKeywordEntity newSearchKeyword = SearchKeywordEntity.builder()
                    .keyword(keyword)
                    .searchCount(1L)
                    .build();

            searchKeywordRepository.save(newSearchKeyword);
            Optional<SearchKeywordEntity> searchKeywordEntity = searchKeywordRepository.findByKeyword(keyword);

            assertThat(searchKeywordEntity.isPresent()).isTrue();
            SearchKeywordEntity searchKeyword = searchKeywordEntity.get();
            assertThat(searchKeyword.getSearchKeywordId()).isEqualTo(12L);
            assertThat(searchKeyword.getKeyword()).isEqualTo(keyword);
        }

        @Test
        @DisplayName("case3. 인기 검색어 조회 (최대 10개)")
        void case3() {
            List<SearchKeywordEntity> searchKeywordEntities = searchKeywordRepository.findTop10ByOrderBySearchCountDesc();

            assertThat(searchKeywordEntities).isNotEmpty();
            SearchKeywordEntity searchKeyword = searchKeywordEntities.get(0);
            assertThat(searchKeyword.getSearchKeywordId()).isEqualTo(10);
            assertThat(searchKeyword.getSearchCount()).isEqualTo(250);
        }
    }
}