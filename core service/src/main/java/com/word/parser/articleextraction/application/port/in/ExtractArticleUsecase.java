package com.word.parser.articleextraction.application.port.in;

import com.word.parser.articleextraction.Article;
import com.word.parser.commons.WebDataExtractor;

public interface ExtractArticleUsecase {
    void extractArticleData(WebDataExtractor webDataExtractor, Article article);
}
