package com.word.parser.articleextraction.adapter.in.web;

import com.word.parser.articleextraction.Article;
import com.word.parser.articleextraction.ArticleJob;
import com.word.parser.articleextraction.adapter.in.web.model.APIResponse;
import com.word.parser.articleextraction.adapter.in.web.model.ExtractionRequest;
import com.word.parser.articleextraction.adapter.out.persistence.BaseWordPersistenceAdapter;
import com.word.parser.articleextraction.adapter.out.persistence.domain.BaseWordPairDomain;
import com.word.parser.articleextraction.application.port.in.ExtractArticleOrchestrationUsecase;
import com.word.parser.articleextraction.application.port.in.ExtractionUsecase;
import com.word.parser.articleextraction.application.port.out.GetArticlePort;
import com.word.parser.articleextraction.application.port.out.SaveLinkDataPort;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ArticleExtractionController {
    public static final String EXTRACT_LINK = "/v1/extract";
    public static final String EXTRACT_ARTICLE = "/v1/article";
    public final ExtractionUsecase extractionUsecase;
    public final ExtractArticleOrchestrationUsecase orchestrationUsecase;

    @PostMapping("/v1/extract")
    public ResponseEntity<APIResponse> extract(@RequestBody ExtractionRequest extractionRequest){
        extractionUsecase.extract(extractionRequest.getCategory(), extractionRequest.getMax());
        return new ResponseEntity<>(new APIResponse("SUCCESS"), HttpStatus.OK);
    }

    @PostMapping("/v1/article")
    public ResponseEntity<APIResponse> extractArticle(@RequestBody ArticleJob articleJob){
        orchestrationUsecase.performArticleJob(articleJob);
        return new ResponseEntity<>(new APIResponse("SUCCESS"), HttpStatus.OK);
    }
}
