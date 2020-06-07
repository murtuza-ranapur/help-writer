package com.word.parser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class CoreService {

    public static void main(String[] args) {
        SpringApplication.run(CoreService.class, args);
    }
}
