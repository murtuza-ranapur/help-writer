package com.word.parser.configurations;

import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;

@Configuration
public class StopWordConfiguration {

    @Bean
    public StopWordRegistery stopWordRegistery(){
        try(InputStream inputStream = StopWordRegistery.class.getResourceAsStream("/stopword.txt")){
            List<String> stopWords = IOUtils.readLines(inputStream, StandardCharsets.UTF_8);
            return new StopWordRegistery(stopWords);
        }catch (Exception e){
            e.printStackTrace();
            return new StopWordRegistery(Collections.emptyList());
        }
    }
}
