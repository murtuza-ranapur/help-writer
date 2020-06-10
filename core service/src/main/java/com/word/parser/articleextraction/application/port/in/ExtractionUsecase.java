package com.word.parser.articleextraction.application.port.in;

import com.word.parser.commons.enums.Category;

public interface ExtractionUsecase {
    void extract(Category category, int max);
}
