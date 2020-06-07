package com.word.parser.articleextraction.application.port.out;

import com.word.parser.articleextraction.Article;

import java.util.List;

public interface SaveLinkDataPort {
    Article save(Article article);
    int saveAll(List<Article> articles);
}
