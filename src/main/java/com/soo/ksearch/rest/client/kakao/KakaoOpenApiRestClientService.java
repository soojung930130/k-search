package com.soo.ksearch.rest.client.kakao;

import com.soo.ksearch.common.exception.ErrorCode;
import com.soo.ksearch.common.exception.SearchApiException;
import com.soo.ksearch.common.exception.SearchApiExceptionBuilder;
import com.soo.ksearch.rest.client.kakao.dto.code.SortType;
import com.soo.ksearch.rest.client.kakao.dto.response.KakaoOpenApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class KakaoOpenApiRestClientService {
    private final RestTemplate restTemplate;
    private final KakaoOpenApiProperties kakaoOpenApiProperties;

    @Autowired
    public KakaoOpenApiRestClientService(RestTemplate restTemplate, KakaoOpenApiProperties kakaoOpenApiProperties) {
        this.restTemplate = restTemplate;
        this.kakaoOpenApiProperties = kakaoOpenApiProperties;
    }

    public KakaoOpenApiResponse searchBlog(String query, SortType sortType, int page, int size) {

        URI uri = UriComponentsBuilder.fromUriString(kakaoOpenApiProperties.getAddress())
                .path("/v2/search/blog")
                .queryParam("query", query)
                .queryParam("sort", sortType)
                .queryParam("page", page)
                .queryParam("size", size)
                .build()
                .toUri();

        HttpEntity<Object> requestEntity = new HttpEntity<>(createHttpHeaders());

        try{
            ResponseEntity<KakaoOpenApiResponse> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, KakaoOpenApiResponse.class);
            if(responseEntity.getStatusCode().isError()) {
                handleError(responseEntity);
            }
            return responseEntity.getBody();

        } catch (RestClientException e){
            throw SearchApiExceptionBuilder.builder(ErrorCode.API_SERVER_ERROR).build();
        }
    }

    private HttpHeaders createHttpHeaders() {
        final HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.AUTHORIZATION, "KakaoAK " + kakaoOpenApiProperties.getToken());
        return httpHeaders;
    }

    private void handleError(ResponseEntity<KakaoOpenApiResponse> response) {
        if (response.getStatusCode().is4xxClientError()) {
            throw new SearchApiException(ErrorCode.API_SERVER_INVALID_REQUEST, null, response.getStatusCode(), null);

        } else if (response.getStatusCode().is5xxServerError()) {
            throw new SearchApiException(ErrorCode.API_SERVER_ERROR, null, response.getStatusCode(), null);
        }
    }
}
