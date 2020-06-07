package com.word.parser.wordrank.retrival;

import com.word.parser.wordrank.retrival.application.in.RetrieveIndexUseCase;
import com.word.parser.wordrank.retrival.application.out.GetWordPort;
import com.word.parser.wordrank.retrival.application.out.GetWordRankPort;
import com.word.parser.wordrank.retrival.application.out.SaveWordPort;
import com.word.parser.wordrank.retrival.domain.Word;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class RankRetrievalService implements RetrieveIndexUseCase {
    private final GetWordPort getWordPort;
    private final GetWordRankPort getWordRankPort;
    private final SaveWordPort saveWordPort;

    @Override
    public void retrieveIndex(Word word) {
        Optional<Word> wordOp = getWordPort.getWordData(word.getWord());
        if(wordOp.isEmpty()) {
            int rank = getWordRankPort.getRank(word.getWord());
            word.setRank(rank);
            log.info("Word '{}' has '{}' rank", word.getWord(), word.getRank());
            wordOp = getWordPort.getWordData(word.getWord()); //Should still be empty
            if(wordOp.isEmpty()) {
                saveWordPort.save(word);
            }
        } else {
            log.info("Rank already found for word : {} -> {}", wordOp.get().getWord(), wordOp.get().getRank());
        }
    }
}
