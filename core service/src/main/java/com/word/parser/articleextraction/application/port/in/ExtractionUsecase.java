package com.word.parser.articleextraction.application.port.in;

import com.word.parser.articleextraction.Category;

public interface ExtractionUsecase {
    void extract(Category category, int max);
}
