package com.word.parser.articleextraction.adapter.in.web.model;

import com.word.parser.articleextraction.Category;
import lombok.Data;

@Data
public class ExtractionRequest {
    private Category category;
    private int max;
}
