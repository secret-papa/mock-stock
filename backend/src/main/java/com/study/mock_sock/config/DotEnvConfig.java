package com.study.mock_sock.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!prod")
public class DotEnvConfig {
    @Bean
    public Dotenv dotenv() {
        return Dotenv.configure().load();
    }
}

