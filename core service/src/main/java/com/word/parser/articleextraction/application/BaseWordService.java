package com.word.parser.articleextraction.application;

import com.word.parser.articleextraction.Sentence;
import com.word.parser.articleextraction.application.port.out.CheckBaseWordPort;
import com.word.parser.articleextraction.application.port.out.SaveBaseWordPort;
import com.word.parser.commons.MerriumBaseWordService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@Service
public class BaseWordService {
    private final MerriumBaseWordService merriumBaseWordService;
    private final SaveBaseWordPort saveBaseWordPort;
    private final CheckBaseWordPort checkBaseWordPort;

    /**
     * This method calls Merrium to extract base word for the given word, if word is not present it
     * will be removed and if present will be replaced with base word
     *
     * @param topWords words found by extracted words
     * @return filtered base word list
     */
    public Map<String, List<Sentence>> returnBaseWords(Map<String, List<Sentence>> topWords) {
        List<CompletableFuture<Map<String,String>>> basewords = new ArrayList<>();
        Map<String, List<Sentence>> filteredList = new HashMap<>();
        for (String word : topWords.keySet()) {
            Optional<String> baseWordOp = checkBaseWordPort.checkIfBaseWordExist(word);
            if(baseWordOp.isPresent()){
                filteredList.put(baseWordOp.get(), topWords.get(word));
            }else {
                basewords.add(merriumBaseWordService.getBaseWord(word));
            }
        }
        for (CompletableFuture<Map<String, String>> baseword : basewords) {
            Map<String, String> wordBaseMap = baseword.join();
            Map.Entry<String, String> entry = wordBaseMap.entrySet().iterator().next();
            if(StringUtils.isNotEmpty(entry.getValue())) {
                filteredList.put(entry.getValue(), topWords.get(entry.getKey()));
                saveBaseWordPort.saveBaseWord(entry.getKey(), entry.getValue());
            }
        }
        return filteredList;
    }

}
