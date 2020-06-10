package com.word.parser.configurations;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StopWordRegistery {

    private final Set<String> stopword;

    public StopWordRegistery(List<String> stopwords) {
        this.stopword = new HashSet<>(stopwords);
    }


    public boolean isStopWord(String word) {
        return stopword.contains(word);
    }

}
