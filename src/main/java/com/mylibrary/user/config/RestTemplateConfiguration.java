package com.mylibrary.user.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Basic configuration of the {@link RestTemplate} used in the project
 *
 */
@SuppressWarnings({"unused"})
@Configuration
public class RestTemplateConfiguration {

    @LoadBalanced
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
