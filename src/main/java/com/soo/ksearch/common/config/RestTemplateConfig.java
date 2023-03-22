package com.soo.ksearch.common.config;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class RestTemplateConfig {

    private static final int DEFAULT_READ_TIMEOUT = 5000;
    private static final int DEFAULT_CONNECT_TIMEOUT = 5000;
    private static final int DEFAULT_MAX_CONNECTION_TOTAL = 120;
    private static final int DEFAULT_MAX_CONNECTION_PER_ROUTE = 30;
    private static final int DEFAULT_CONNECTION_REQUEST_TIMEOUT = 5000;

    @Bean
    public HttpComponentsClientHttpRequestFactory defaultHttpRequestFactory() {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setMaxConnTotal(DEFAULT_MAX_CONNECTION_TOTAL)
                .setMaxConnPerRoute(DEFAULT_MAX_CONNECTION_PER_ROUTE)
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(DEFAULT_READ_TIMEOUT);
        requestFactory.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT);
        requestFactory.setConnectionRequestTimeout(DEFAULT_CONNECTION_REQUEST_TIMEOUT);
        requestFactory.setHttpClient(httpClient);

        return requestFactory;
    }

    @Bean
    public RestTemplate defaultRestTemplate(@Qualifier("restTemplateLoggingInterceptor") RestTemplateLoggingInterceptor restTemplateLoggingInterceptor) {

        RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(defaultHttpRequestFactory()));
        restTemplate.setInterceptors(List.of(restTemplateLoggingInterceptor));

        return restTemplate;
    }
}
