package com.word.parser.articleextraction.application.port.out;

import com.word.parser.articleextraction.Word;

public interface PublishDataPort {
    void pushWord(Word word);
}
