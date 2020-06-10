package com.word.parser.articleextraction.adapter.out.persistence.domain;

import com.word.parser.commons.enums.Category;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(collection = "article")
@Data
public class ArticleDomain {
    @Id
    private String id;
    @Indexed(unique = true)
    private String url;
    private Category category;
    private String website;
    private String data;
    private boolean isProcessed;
    private Map<String, List<SentenceDomain>> topWords;
}
