package com.word.parser.articleextraction.application.port.out;

import java.util.Optional;

public interface CheckBaseWordPort {
    Optional<String> checkIfBaseWordExist(String word);
}
