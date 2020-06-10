package com.word.parser.commons;

import com.word.parser.commons.enums.Category;
import lombok.SneakyThrows;
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
public class IGNDataExtractor implements WebDataExtractor {
    private final static String WEB_URL = "https://www.ign.com/reviews/movies";
    private static final String SECTION = "jsx-3995683049";
    private final static String ARTICLE = "article";
    private final static String LINK = "./div/div/a";
    private final static String JSOUP_SELECTOR = "#main-content > div > div.jsx-2880685507.page-content > div > div.jsx-2880685507.article-content.page-0";

    @Override
    public List<String> getLinks(WebManager webManager, int max) {
        Set<String> links = new HashSet<>();
        webManager.open(getUrl());
        List<WebElement> articles = webManager.getElementsByTag(ARTICLE);
        int currentElements = articles.size();
        int previous = 0;
        int count = 0;
        int section = 1;
        while (currentElements != previous && count < max) {
            for (int i = 0; i < articles.size() && count < max; i++) {
                WebElement article = webManager.getElementByXPath(articles.get(i),LINK);
                links.add(article.getAttribute("href"));
                count++;
            }
            webManager.scrollToElement(articles.get(articles.size()-1));
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            List<WebElement> sections = webManager.getElementByClass(SECTION);
            if(sections.size() != section+1){
                break;
            }
            articles = webManager.getElementsByTag(sections.get(section),ARTICLE);
            previous = currentElements;
            currentElements = articles.size()+10;
            section++;
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
