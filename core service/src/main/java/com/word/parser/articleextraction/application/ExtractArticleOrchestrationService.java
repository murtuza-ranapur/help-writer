package com.word.parser.articleextraction.application;

import com.word.parser.articleextraction.Article;
import com.word.parser.articleextraction.ArticleJob;
import com.word.parser.articleextraction.application.port.in.ExtractArticleOrchestrationUsecase;
import com.word.parser.articleextraction.application.port.in.ExtractArticleUsecase;
import com.word.parser.articleextraction.application.port.out.GetArticlePort;
import com.word.parser.commons.WebDataExtractor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class ExtractArticleOrchestrationService implements ExtractArticleOrchestrationUsecase {
    private final List<WebDataExtractor> extractors;
    private final Map<String, WebDataExtractor> websiteWiseMap;
    private final GetArticlePort getArticlePort;
    private final ExtractArticleUsecase extractArticleUsecase;

    public ExtractArticleOrchestrationService(List<WebDataExtractor> extractors, GetArticlePort getArticlePort, ExtractArticleUsecase extractArticleUsecase) {
        this.extractors = extractors;
        this.extractArticleUsecase = extractArticleUsecase;
        this.websiteWiseMap = new HashMap<>();
        this.getArticlePort = getArticlePort;
        for (WebDataExtractor extractor : this.extractors) {
            websiteWiseMap.put(extractor.getUrl(),extractor);
        }
    }

    @Async
    @Override
    public void performArticleJob(ArticleJob articleJob) {
        Set<String> articleSet = articleJob.getArticles();
        for (String articleUrl : articleSet) {
            Optional<Article> articleOp = getArticlePort.getByArticleURL(articleUrl);
            if(articleOp.isPresent()){
                extractArticleUsecase.extractArticleData(websiteWiseMap.get(articleOp.get().getWebsite()),
                        articleOp.get());
            }else {
                log.error("Article {} no longer present", articleUrl);
            }
        }
    }
}
