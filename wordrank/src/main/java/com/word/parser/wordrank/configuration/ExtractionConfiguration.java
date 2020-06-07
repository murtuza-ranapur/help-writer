package com.word.parser.wordrank.configuration;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExtractionConfiguration {

    @Bean
    public WebManager webManager(){
        WebDriverManager.chromedriver().setup();
        return WebManager.newInstance();
    }
}
