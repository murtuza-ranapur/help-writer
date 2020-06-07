package com.word.parser.articleextraction.application;

import com.word.parser.articleextraction.Article;
import com.word.parser.articleextraction.ArticleJob;
import com.word.parser.articleextraction.Category;
import com.word.parser.articleextraction.application.port.in.ExtractionUsecase;
import com.word.parser.articleextraction.application.port.in.LinkExtractionUsecase;
import com.word.parser.articleextraction.application.port.out.GetArticlePort;
import com.word.parser.commons.WebDataExtractor;
import com.word.parser.zookeeper.ServiceRegistry;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExtractionOrchestration implements ExtractionUsecase {
    private final List<WebDataExtractor> extractors;
    private final Map<Category, List<WebDataExtractor>> categoryWiseExtractorMap;
    private final LinkExtractionUsecase linkExtractionUsecase;
    private final GetArticlePort getArticlePort;
    private final ServiceRegistry serviceRegistry;

    public ExtractionOrchestration(List<WebDataExtractor> extractors, LinkExtractionUsecase linkExtractionUsecase,
                                   GetArticlePort getArticlePort, @Qualifier("slave") ServiceRegistry serviceRegistry) {
        this.extractors = extractors;
        this.linkExtractionUsecase = linkExtractionUsecase;
        this.getArticlePort = getArticlePort;
        this.serviceRegistry = serviceRegistry;
        this.categoryWiseExtractorMap = new EnumMap<>(Category.class);
        for (WebDataExtractor extractor : extractors) {
            List<WebDataExtractor> categoryExtractors = categoryWiseExtractorMap.getOrDefault(extractor.getCategory(),
                    new ArrayList<>());
            categoryExtractors.add(extractor);
            categoryWiseExtractorMap.put(extractor.getCategory(), categoryExtractors);
        }
    }

    @Async
    @Override
    public void extract(Category category, int max) {
        if (category == Category.ALL) {
            List<WebDataExtractor> extractorsAll = categoryWiseExtractorMap.values().stream()
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
            extractLinks(extractorsAll, max);
        } else {
            List<WebDataExtractor> extractors = categoryWiseExtractorMap.get(category);
            extractLinks(extractors, max);
        }
        splitWorkAmongSlaves(category);
    }

    private void splitWorkAmongSlaves(Category category) {
        try {
            Set<Article> articles = getArticlePort.getUnprocessedArticleByCategory(category);
            List<String> availableNodes = serviceRegistry.getAllServiceAddresses();
            log.info("Number of available nodes {}", availableNodes.size());
            Map<String, ArticleJob> articleSplit = getTaskNodeMap(availableNodes, articles);
            splitWork(articleSplit);
        } catch (KeeperException | InterruptedException e) {
            log.error("No Worker node available at moment",e);
        }
    }

    private void splitWork(Map<String, ArticleJob> articleSplit) {
        RestTemplate restTemplate = new RestTemplate();
        for (Map.Entry<String, ArticleJob> stringArticleJobEntry : articleSplit.entrySet()) {
            ResponseEntity<Void> responseEntity =
                    restTemplate.postForEntity(stringArticleJobEntry.getKey(),
                            stringArticleJobEntry.getValue(), Void.class);
            if(responseEntity.getStatusCode() == HttpStatus.OK){
                log.info("Job initiated. Sent {} articles to {}", stringArticleJobEntry.getValue().getArticles().size(),
                        stringArticleJobEntry.getKey());
            }else{
                log.error("Job failed for url {}", stringArticleJobEntry.getKey());
            }
        }
    }

    private Map<String, ArticleJob> getTaskNodeMap(List<String> availableNodes, Set<Article> articles) {
        Map<String, ArticleJob> taskNodeMap = new HashMap<>();
        int maxArticleSplit = (int) Math.ceil((double) articles.size()/availableNodes.size());
        log.info("Each node will have : {} articles to process", maxArticleSplit);
        Iterator<Article> booksIterator = articles.iterator();
        for (String availableNode : availableNodes) {
            int count = maxArticleSplit;
            Set<String> articleSet = new HashSet<>();
            if(booksIterator.hasNext()) {
                while (booksIterator.hasNext() && count>0){
                    articleSet.add(booksIterator.next().getUrl());
                    count--;
                }
            }else{
                break;
            }
            taskNodeMap.put(availableNode, new ArticleJob(articleSet));
        }
        return taskNodeMap;
    }

    private void extractLinks(List<WebDataExtractor> extractors, int max){
        for (WebDataExtractor extractor : extractors) {
            linkExtractionUsecase.extractLinks(extractor, max);
        }
    }

}
