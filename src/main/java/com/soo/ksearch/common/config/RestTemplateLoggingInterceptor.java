package com.soo.ksearch.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class RestTemplateLoggingInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

        logRequest(request, body);

        try {
            final ClientHttpResponse response = execution.execute(request, body);
            logResponse(response);
            return response;
        } catch (IOException e) {
            log.error("failed to communicate", e);
            throw e;
        }
    }

    private void logRequest(HttpRequest request, byte[] body) {
        log.debug("--------------- request info  ---------------");
        log.debug("URI     : {}", request.getURI());
        log.debug("Method  : {}", request.getMethod());
        log.debug("Headers : {}", request.getHeaders());
        log.debug("Body    : {}", new String(body, StandardCharsets.UTF_8));
        log.debug("---------------------------------------------");
    }

    private void logResponse(ClientHttpResponse response) throws IOException {
        log.debug("--------------- response info  ---------------");
        log.debug("Status code : {}", response.getStatusCode());
        log.debug("Headers     : {}", response.getHeaders());
        log.debug("Body        : {}", new String(response.getBody().readAllBytes(), StandardCharsets.UTF_8));
        log.debug("---------------------------------------------");
    }
}
