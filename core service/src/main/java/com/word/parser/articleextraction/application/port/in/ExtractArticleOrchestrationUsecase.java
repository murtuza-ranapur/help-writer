package com.word.parser.articleextraction.application.port.in;

import com.word.parser.articleextraction.ArticleJob;

public interface ExtractArticleOrchestrationUsecase {
    void performArticleJob(ArticleJob articleJob);
}
