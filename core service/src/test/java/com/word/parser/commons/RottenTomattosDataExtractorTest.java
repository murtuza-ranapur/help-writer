package com.word.parser.commons;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RottenTomattosDataExtractorTest {
    @BeforeAll
    public static void setup(){
        WebDriverManager.chromedriver().setup();
    }

    @Test
    void getLinks() {
        WebManager webManager = WebManager.newInstance();
        try {
            RottenTomattosDataExtractor tomattosDataExtractor = new RottenTomattosDataExtractor();
            List<String> articles = tomattosDataExtractor.getLinks(webManager, 20);
            articles.forEach(System.out::println);
            webManager.disconnect();
            assertTrue(articles.size() > 0 && articles.size() <= 20);
        }catch (Exception e){
            webManager.disconnect();
            throw  e;
        }

    }

    @Test
    void getArticle() {
        RottenTomattosDataExtractor tomattosDataExtractor = new RottenTomattosDataExtractor();
        String data = tomattosDataExtractor.getArticleData(
                "https://www.rottentomatoes.com/tv/jeffrey_epstein_filthy_rich/s01");
        System.out.println(data);
        assertNotNull(data);
    }
}
