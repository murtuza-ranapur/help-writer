package com.word.parser.commons;

import com.word.parser.commons.enums.Category;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Slf4j
public class TheHinduDataExtractor implements WebDataExtractor {
    private static final String WEB_URL = "https://www.thehindu.com/opinion/editorial/";
    private static final String ARTICLE_DIV_CLASS = "story-card";
    private static final String ARTICLE_DIV_CLASS_1 = "Other-StoryCard";
    private static final String LINK = "./a";
    private static final String SECTION_ID = "//*[@id=\"section_2\"]/div[2]/div";
    private final static String JSOUP_SELECTOR = "body > div.container-main > div.jscroll > div > div > div > section > div > div > div > div:nth-child(3)";

    @Override
    public List<String> getLinks(WebManager webManager, int max) {
        Set<String> links = new HashSet<>();
        webManager.open(getUrl());
        WebElement section = webManager.getElementByXPath(SECTION_ID);
        webManager.scrollToElement(section);
        List<WebElement> articles = webManager.getElementByClass(ARTICLE_DIV_CLASS);
        articles.addAll(webManager.getElementByClass(ARTICLE_DIV_CLASS_1));
        log.info("Total article elements : {}",articles.size());
        int size = Math.min(max, articles.size());
        for (int i = 0; i < size; i++) {
            try {
                WebElement article = webManager.getElementByXPath(articles.get(i), LINK);
                links.add(article.getAttribute("href"));
            }catch (Exception e){
                if(articles.size() > size){
                    size++;
                }
            }
        }
        return new ArrayList<>(links);
    }

    @SneakyThrows
    @Override
    public String getArticleData(String link) {
        Document doc = Jsoup.connect(link).get();
        Elements body = doc.select(JSOUP_SELECTOR);
        return body.text();
    }

    @Override
    public Category getCategory() {
        return Category.NEWS;
    }

    @Override
    public String getUrl() {
        return WEB_URL;
    }
}
