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
public class RottenTomattosDataExtractor implements WebDataExtractor{
    private static final String WEB_URL = "https://www.rottentomatoes.com/browse/tv-list-2";
    private static final String ARTICLE_DIV_CLASS = "movie_info";
    private static final String LINK = "./a";
    private static final String VISIBLE_SECTION = "//*[@id=\"content-column\"]/div[2]/div[2]";
    private static final String JSOUP_SELECTOR = "#topSection > div.tv-season-top-section__ratings-group > div > section > div.mop-ratings-wrap.score_panel.js-mop-ratings-wrap > section > p";


    @Override
    public List<String> getLinks(WebManager webManager, int max) {
        Set<String> links = new HashSet<>();
        webManager.open(getUrl());
        webManager.waitForElement(VISIBLE_SECTION);
        List<WebElement> articles = webManager.getElementByClass(ARTICLE_DIV_CLASS);
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
        return Category.MOVIE;
    }

    @Override
    public String getUrl() {
        return WEB_URL;
    }
}
