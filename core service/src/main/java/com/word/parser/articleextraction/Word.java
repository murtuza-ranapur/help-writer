package com.word.parser.articleextraction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Word {
    private String word;
    private String articleUrl;
    private List<Sentence> sentences;

}
