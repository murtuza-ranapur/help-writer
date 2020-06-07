package com.word.parser.articleextraction.application.port.in;

import com.word.parser.articleextraction.Article;
import com.word.parser.commons.WebDataExtractor;

import java.util.List;

public interface LinkExtractionUsecase {
    List<Article> extractLinks(WebDataExtractor webDataExtractor, int maxlinks);
}
