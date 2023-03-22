package com.soo.ksearch.blog.service;

import com.soo.ksearch.blog.dto.SearchKeyword;
import com.soo.ksearch.blog.mapper.rest.BlogSearchRestMapper;
import com.soo.ksearch.common.db.entity.SearchKeywordEntity;
import com.soo.ksearch.common.db.repository.SearchKeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BlogSearchService {
    private final BlogSearchRestMapper blogSearchRestMapper;
    private final SearchKeywordRepository searchKeywordRepository;

    @Transactional
    public SearchKeywordEntity saveSearchHistory(String keyword) {
        Optional<SearchKeywordEntity> searchKeywordEntity = searchKeywordRepository.findByKeyword(keyword);

        if (searchKeywordEntity.isPresent()) {
            SearchKeywordEntity searchKeyword = searchKeywordEntity.get();
            searchKeyword.setSearchCount(searchKeyword.getSearchCount() + 1);
            return searchKeyword;
        } else {
            SearchKeywordEntity searchKeyword = SearchKeywordEntity.builder()
                    .keyword(keyword)
                    .searchCount(1L)
                    .build();
            return searchKeywordRepository.save(searchKeyword);
        }
    }

    public List<SearchKeyword> findTop10SearchKeyword() {
        return searchKeywordRepository.findTop10ByOrderBySearchCountDesc()
                .stream()
                .map(blogSearchRestMapper::toDto)
                .collect(Collectors.toList());
    }
}
