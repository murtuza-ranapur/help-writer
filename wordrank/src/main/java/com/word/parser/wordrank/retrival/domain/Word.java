package com.word.parser.wordrank.retrival.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Word {
    private String id;
    private String word;
    private String articleUrl;
    private int rank;
    private List<Sentence> sentences;

}
