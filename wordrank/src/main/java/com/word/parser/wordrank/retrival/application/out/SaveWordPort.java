package com.word.parser.wordrank.retrival.application.out;

import com.word.parser.wordrank.retrival.domain.Word;

public interface SaveWordPort {
    void save(Word word);
}
