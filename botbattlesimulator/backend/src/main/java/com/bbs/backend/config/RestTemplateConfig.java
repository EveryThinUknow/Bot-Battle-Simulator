package com.bbs.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

//与另一个spring“matchingsystem”交互 (RestTemplate)
@Configuration
public class RestTemplateConfig {
    @Bean//方便 WebSocketServer 在spring容器中取得该方法
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }
}
