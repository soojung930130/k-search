package com.soo.ksearch.rest.client.kakao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConstructorBinding
@ConfigurationProperties(prefix = "rest.client.kakao-api")
@AllArgsConstructor
public class KakaoOpenApiProperties {
    String token;
    String address;
}
