package com.word.parser.articleextraction;

import com.word.parser.commons.enums.Category;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class Article {
    @EqualsAndHashCode.Exclude
    private String id;
    private String url;
    private Category category;
    private String website;
    @EqualsAndHashCode.Exclude
    private String data;
    @EqualsAndHashCode.Exclude
    private Map<String, List<Sentence>> topWords;
    @EqualsAndHashCode.Exclude
    private boolean isProcessed;

    public Article(String url, Category category, String website) {
        this.url = url;
        this.category = category;
        this.website = website;
    }

}
