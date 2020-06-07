package com.word.parser.wordrank.retrival.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class Sentence {
    private int index;
    private String sentence;
}
