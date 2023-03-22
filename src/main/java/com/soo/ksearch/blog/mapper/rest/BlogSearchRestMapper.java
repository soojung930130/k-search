package com.soo.ksearch.blog.mapper.rest;

import com.soo.ksearch.blog.dto.SearchKeyword;
import com.soo.ksearch.blog.dto.response.PopularKeywordResponse;
import com.soo.ksearch.blog.dto.response.SearchBlogResponse;
import com.soo.ksearch.common.db.entity.SearchKeywordEntity;
import com.soo.ksearch.common.mapper.CommonRestMapperConfig;
import com.soo.ksearch.rest.client.kakao.dto.response.KakaoOpenApiResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        config = CommonRestMapperConfig.class)
public interface BlogSearchRestMapper {

    SearchBlogResponse toSearchBlogsResponseRestDto(KakaoOpenApiResponse.Documents documents);

    SearchKeyword toDto(SearchKeywordEntity searchKeywordEntity);

    PopularKeywordResponse toPopularKeywordResponseRestDto(SearchKeyword keyword);
}
