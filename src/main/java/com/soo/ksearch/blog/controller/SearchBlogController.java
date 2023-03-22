package com.soo.ksearch.blog.controller;

import com.soo.ksearch.blog.dto.response.PopularKeywordResponse;
import com.soo.ksearch.blog.dto.response.SearchBlogResponse;
import com.soo.ksearch.blog.mapper.rest.BlogSearchRestMapper;
import com.soo.ksearch.blog.service.BlogSearchService;
import com.soo.ksearch.rest.client.kakao.KakaoOpenApiRestClientService;
import com.soo.ksearch.rest.client.kakao.dto.code.SortType;
import com.soo.ksearch.rest.client.kakao.dto.response.KakaoOpenApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Blog", description = "블로그 검색 API")
@Slf4j
@RestController
@RequestMapping("/search-api/v1/blog")
@Valid
@RequiredArgsConstructor
public class SearchBlogController {

    private final KakaoOpenApiRestClientService kakaoOpenApiRestClientService;
    private final BlogSearchService blogSearchService;
    private final BlogSearchRestMapper blogSearchRestMapper;

    @Operation(summary = "블로그 검색", description = "키워드를 통해 블로그를 검색")
    @GetMapping(path = "/blogs")
    public Page<SearchBlogResponse> searchBlogs(
            @Parameter(description = "페이지 번호. 1부터 시작", required = true, example = "1")
            @RequestParam @Min(1) @Max(50) int pageNumber,
            @Parameter(description = "페이지 크기", required = true, example = "10")
            @RequestParam @Min(1) @Max(50) int pageSize,
            @Parameter(description = "검색 키워드.", required = true, example = "ChatGPT")
            @RequestParam String keyword,
            @Parameter(description = "정렬 방식. accuracy(정확도순), recency(최신순) 지원", example = "accuracy")
            @RequestParam(defaultValue = "accuracy") String sortType
    ) {

        KakaoOpenApiResponse kakaoOpenApiResponse = kakaoOpenApiRestClientService.searchBlog(keyword, SortType.of(sortType), pageNumber, pageSize);
        KakaoOpenApiResponse.Meta meta = kakaoOpenApiResponse.getMeta();

        List<SearchBlogResponse> blogs = kakaoOpenApiResponse.getDocuments()
                .stream()
                .map(blogSearchRestMapper::toSearchBlogsResponseRestDto)
                .collect(Collectors.toList());

        blogSearchService.saveSearchHistory(keyword);

        return new PageImpl<>(blogs, PageRequest.of(pageNumber, pageSize), meta.getTotalCount());
    }

    @Operation(summary = "인기 검색어 목록")
    @GetMapping(path = "/favorite-keywords")
    public List<PopularKeywordResponse> searchTop10SearchKeyword() {
        return blogSearchService.findTop10SearchKeyword()
                .stream()
                .map(blogSearchRestMapper::toPopularKeywordResponseRestDto)
                .collect(Collectors.toList());
    }
}
