package com.word.parser.articleextraction.application;

import com.word.parser.articleextraction.Article;
import com.word.parser.articleextraction.application.port.in.LinkExtractionUsecase;
import com.word.parser.articleextraction.application.port.out.GetArticlePort;
import com.word.parser.articleextraction.application.port.out.SaveLinkDataPort;
import com.word.parser.commons.WebDataExtractor;
import com.word.parser.commons.WebManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class ExtractLinkService implements LinkExtractionUsecase {
    public final SaveLinkDataPort saveLinkDataPort;
    public final GetArticlePort getArticlePort;

    @Override
    public List<Article> extractLinks(WebDataExtractor webDataExtractor, int maxlinks) {
        log.info("Initiating link extraction for url {}",webDataExtractor.getUrl());
        WebManager webManager = WebManager.newInstance();
        if(webDataExtractor.isLoginRequired()){
            webDataExtractor.login(webManager);
        }
        List<String> articleUrls = webDataExtractor.getLinks(webManager, maxlinks);
        List<Article> articles = articleUrls.stream()
                .map(url -> new Article(url,webDataExtractor.getCategory(), webDataExtractor.getUrl()))
                .collect(Collectors.toList());
        Set<Article> saved = getArticlePort.getByArticleWebURL(webDataExtractor.getUrl());
        articles = filterNotPresent(articles, saved);
        log.info("Retrived total : {} articles", articles.size());
        saveLinkDataPort.saveAll(articles);
        webManager.disconnect();
        return articles;
    }

    private List<Article> filterNotPresent(List<Article> articles, Set<Article> saved){
        List<Article> filtered = new ArrayList<>();
        for (Article article : articles) {
            if(!saved.contains(article)){
                filtered.add(article);
            }
        }
        return filtered;
    }
}
