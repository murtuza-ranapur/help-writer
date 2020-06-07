package com.word.parser.wordrank.retrival.adapter.out.persistence.model;

import com.word.parser.wordrank.retrival.domain.Sentence;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "word")
@Data
public class WordEntity {
    @Id
    private String id;
    private String word;
    private String articleUrl;
    private int rank;
    private List<Sentence> sentences;
}
