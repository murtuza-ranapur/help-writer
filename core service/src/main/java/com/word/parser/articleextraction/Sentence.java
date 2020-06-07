package com.word.parser.articleextraction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class Sentence {
    private int index;
    private String sentence;
}
