package com.word.parser.commons.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
public class AsyncThreadingConfiguration {
    @Bean
    public Executor taskExecutor() {
        return Executors.newFixedThreadPool(2);
    }
}
