package com.word.parser.commons;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TheHinduDataExtractorTest {
    @BeforeAll
    public static void setup(){
        WebDriverManager.chromedriver().setup();
    }

    @Test
    void getLinks() {
        WebManager webManager = WebManager.newInstance();
        try {
            TheHinduDataExtractor theHinduDataExtractor = new TheHinduDataExtractor();
            List<String> articles = theHinduDataExtractor.getLinks(webManager, 20);
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
        TheHinduDataExtractor theHinduDataExtractor = new TheHinduDataExtractor();
        String data = theHinduDataExtractor.getArticleData(
                "https://www.thehindu.com/opinion/editorial/rising-tide-the-hindu-editorial-on-return-of-protests-in-hong-kong/article31689909.ece");
        System.out.println(data);
        assertNotNull(data);
    }
}
