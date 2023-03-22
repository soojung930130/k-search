package com.soo.ksearch.common.db.repository;

import com.soo.ksearch.common.db.entity.SearchKeywordEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SearchKeywordRepository extends JpaRepository<SearchKeywordEntity, Long> {

    Optional<SearchKeywordEntity> findByKeyword(String keyword);

    List<SearchKeywordEntity> findTop10ByOrderBySearchCountDesc();
}
