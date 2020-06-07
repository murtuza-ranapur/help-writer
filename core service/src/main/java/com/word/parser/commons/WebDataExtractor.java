package com.word.parser.commons;

import com.word.parser.articleextraction.Category;

import java.util.List;

public interface WebDataExtractor {
    List<String> getLinks(WebManager webManager, int max);
    String getArticleData(String link);
    Category getCategory();
    String getUrl();
    default boolean isLoginRequired(){
        return false;
    };
    default void login(WebManager webManager){
    };
}
