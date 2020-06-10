package com.word.parser.commons;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class MerriumBaseWordService {
    public static final Logger log = LoggerFactory.getLogger(MerriumBaseWordService.class);
    public static final String MERRIUM_URL = "https://www.merriam-webster.com/dictionary/";
    public static final String BASE_SELECTOR = "#left-content > div.row.entry-header > div:nth-child(1) > h1";

    @Async
    public CompletableFuture<Map<String, String>> getBaseWord(String word){
        Map<String, String> baseWordMap = new HashMap<>();
        try {
            Document doc = Jsoup.connect(MERRIUM_URL+word).get();
            Elements body = doc.select(BASE_SELECTOR);
            baseWordMap.put(word, body.text());
        } catch (IOException e) {
            log.error("Failed to find base for "+word);
            baseWordMap.put(word,"");
        }
        return CompletableFuture.completedFuture(baseWordMap);
    }
}
