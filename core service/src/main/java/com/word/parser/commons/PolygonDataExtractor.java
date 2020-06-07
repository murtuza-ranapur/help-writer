package com.word.parser.commons;

import com.word.parser.articleextraction.Category;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Manager;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class PolygonDataExtractor implements WebDataExtractor {
    private static final String WEB_URL = "https://www.polygon.com/news";
    private static final String ARTICLE_CLASS = "c-compact-river__entry";
    private static final String LINK = "./div/a";
    private static final String VISIBLE_SECTION = "//*[@id=\"content\"]/div[3]/div/div[1]/div[1]";
    private static final String JSOUP_SELECTOR = "#content > article > div.l-sidebar-fixed.l-segment.l-article-body-segment > div.l-col__main > div.c-entry-content";

    @Override
    public List<String> getLinks(WebManager webManager, int max) {
        Set<String> links = new HashSet<>();
        webManager.open(getUrl());
        webManager.waitForElement(VISIBLE_SECTION);
        List<WebElement> articles = webManager.getElementByClass(ARTICLE_CLASS);
        log.info("Total article elements : {}",articles.size());
        int size = Math.min(max, articles.size());
        for (int i = 0; i < size; i++) {
            WebElement article = webManager.getElementByXPath(articles.get(i), LINK);
            links.add(article.getAttribute("href"));
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
        return Category.GAMING;
    }

    @Override
    public String getUrl() {
        return WEB_URL;
    }
}
