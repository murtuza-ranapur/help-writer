package com.word.parser.commons;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IGNDataExtractorTest {

    @BeforeAll
    public static void setup(){
        WebDriverManager.chromedriver().setup();
    }

    @Test
    void getLinks() {
        WebManager webManager = WebManager.newInstance();
        IGNDataExtractor ignDataExtractor = new IGNDataExtractor();
        List<String> articles = ignDataExtractor.getLinks(webManager,20);
        articles.forEach(System.out::println);
        webManager.disconnect();
        assertEquals(20,articles.size());
    }

    @Test
    void getArticle() {
        IGNDataExtractor ignDataExtractor = new IGNDataExtractor();
        String data = ignDataExtractor.getArticleData(
                "https://www.ign.com/articles/unbreakable-kimmy-schmidt-kimmy-vs-the-reverend-review-netflix");
        System.out.println(data);
        assertNotNull(data);
    }

}