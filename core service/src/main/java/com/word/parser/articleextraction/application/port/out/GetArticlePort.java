package com.word.parser.articleextraction.application.port.out;

import com.word.parser.articleextraction.Article;
import com.word.parser.commons.enums.Category;

import java.util.Optional;
import java.util.Set;

public interface GetArticlePort {
    Optional<Article> getByArticleURL(String url);
    Set<Article> getByArticleWebURL(String webUrl);
    Set<Article> getUnprocessedArticleByCategory(Category category);
}
