package com.word.parser.articleextraction.adapter.out.persistence.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "baseword")
@NoArgsConstructor
@Data
public class BaseWordPairDomain {
    @Id
    private String id;
    @Indexed(unique = true)
    private String word;
    private String baseWord;

    public BaseWordPairDomain(String word, String baseWord) {
        this.word = word;
        this.baseWord = baseWord;
    }
}
