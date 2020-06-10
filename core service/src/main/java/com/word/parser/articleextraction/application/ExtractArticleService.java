package com.word.parser.articleextraction.application;

import com.word.parser.articleextraction.Article;
import com.word.parser.articleextraction.Sentence;
import com.word.parser.articleextraction.Word;
import com.word.parser.articleextraction.application.port.in.ExtractArticleUsecase;
import com.word.parser.articleextraction.application.port.out.PublishDataPort;
import com.word.parser.articleextraction.application.port.out.SaveLinkDataPort;
import com.word.parser.commons.WebDataExtractor;
import com.word.parser.configurations.StopWordRegistery;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@AllArgsConstructor
@Service
public class ExtractArticleService implements ExtractArticleUsecase {
    public static final int MAX_WORDS = 50;
    public final SaveLinkDataPort saveLinkDataPort;
    public final StopWordRegistery stopWordRegistery;
    public final BaseWordService baseWordService;
    public final PublishDataPort publishDataPort;

    @Override
    public void extractArticleData(WebDataExtractor webDataExtractor, Article article) {
        String data = webDataExtractor.getArticleData(article.getUrl());
        Map<String, List<Sentence>> wordSentenceMap = new HashMap<>();
        Map<String, Integer> wordCountMap = new HashMap<>();
        String sanitizedData = sanitizeData(data);
        article.setData(sanitizedData);
        String [] sentences = sanitizedData.split("\\.");
        for (String sentence : sentences) {
            String[] words = sentence.split("\\s+");
            for (int i=0; i<words.length;i++) {
                String word = words[i];
                if(!stopWordRegistery.isStopWord(word.toLowerCase())) {
                    List<Sentence> wordSentences = wordSentenceMap.getOrDefault(word, new LinkedList<>());
                    wordSentences.add(new Sentence(i,sentence));
                    wordSentenceMap.put(word, wordSentences);
                    wordCountMap.put(word, wordCountMap.getOrDefault(word, 0) + 1);
                }
            }
        }
        Map<String, List<Sentence>> topWords = filterLeastFrequentWord(wordCountMap, wordSentenceMap);
        //Find base word
        Map<String, List<Sentence>> filteredWords = baseWordService.returnBaseWords(topWords);
        article.setTopWords(filteredWords);
        article.setProcessed(true);
        saveLinkDataPort.save(article);
        //Push Word on to the Queue
        publishData(article);
    }

    private void publishData(Article article) {
        for (Map.Entry<String, List<Sentence>> stringListEntry : article.getTopWords().entrySet()) {
            String word =  stringListEntry.getKey();
            List<Sentence> sentences = stringListEntry.getValue();
            Word wordWhole = new Word(word, article.getUrl(), sentences);
            publishDataPort.pushWord(wordWhole);
        }
    }

    private Map<String, List<Sentence>> filterLeastFrequentWord(Map<String, Integer> wordCountMap,
                                                              Map<String, List<Sentence>> wordSentenceMap) {
        Map<String, List<Sentence>> topWordMap = new HashMap<>();
        Comparator<Map.Entry<String, Integer>> valueComparator = (e1, e2) -> {
            Integer v1 = e1.getValue();
            Integer v2 = e2.getValue();
            return v1.compareTo(v2);
        };
        Set<Map.Entry<String, Integer>> entry =  wordCountMap.entrySet();
        List<Map.Entry<String, Integer>> listOfEntries = new ArrayList<>(entry);
        listOfEntries.sort(valueComparator);
        int max = Math.min(MAX_WORDS, listOfEntries.size());
        for (int i = 0; i < max; i++) {
            String key = listOfEntries.get(i).getKey();
            topWordMap.put(key, wordSentenceMap.get(key));
        }
        return topWordMap;
    }

    private String sanitizeData(String data) {
        String unwantedSymbols = "[^a-zA-Z\\s.]";
        return data.replaceAll(unwantedSymbols, "");
    }
}
