package se.autocorrect.springexample.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class HttpClientConfig {

    @Bean
    RestTemplate restTemplate() {
        // Create a plain RestTemplate; customize (timeouts, request factory) here if needed.
        return new RestTemplate();
    }
}
