package com.word.parser.commons.configurations;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class WebDriverConfiguration {

    @PostConstruct
    public void setup(){
        WebDriverManager.chromedriver().setup();
    }
}
