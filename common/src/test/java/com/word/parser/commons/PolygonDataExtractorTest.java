package com.word.parser.commons;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PolygonDataExtractorTest {
    @BeforeAll
    public static void setup(){
        WebDriverManager.chromedriver().setup();
    }

    @Test
    void getLinks() {
        WebManager webManager = WebManager.newInstance();
        try {
            PolygonDataExtractor polygonDataExtractor = new PolygonDataExtractor();
            List<String> articles = polygonDataExtractor.getLinks(webManager, 20);
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
        PolygonDataExtractor polygonDataExtractor = new PolygonDataExtractor();
        String data = polygonDataExtractor.getArticleData(
                "https://www.polygon.com/2020/5/29/21274892/captain-price-cameo-veterans-call-of-duty-endowment-charity");
        System.out.println(data);
        assertNotNull(data);
    }
}
