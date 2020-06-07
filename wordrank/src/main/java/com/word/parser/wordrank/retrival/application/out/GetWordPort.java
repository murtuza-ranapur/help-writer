package com.word.parser.wordrank.retrival.application.out;

import com.word.parser.wordrank.retrival.domain.Word;

import java.util.Optional;

public interface GetWordPort {
    Optional<Word> getWordData(String word);
}
