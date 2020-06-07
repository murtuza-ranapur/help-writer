package com.word.parser.wordrank.retrival.adapter.out.persistence;

import com.word.parser.wordrank.retrival.adapter.out.persistence.mapper.WordDomainMapper;
import com.word.parser.wordrank.retrival.adapter.out.persistence.model.WordEntity;
import com.word.parser.wordrank.retrival.adapter.out.persistence.repository.WordEntityRepository;
import com.word.parser.wordrank.retrival.application.out.GetWordPort;
import com.word.parser.wordrank.retrival.application.out.SaveWordPort;
import com.word.parser.wordrank.retrival.domain.Word;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class WordAdapter implements GetWordPort, SaveWordPort {
    private final WordEntityRepository repository;
    private final WordDomainMapper mapper;

    @Override
    public Optional<Word> getWordData(String word) {
        return repository.findByWord(word).map(mapper::toDomain);
    }

    @Override
    public void save(Word word) {
        WordEntity wordEntity = mapper.toEntity(word);
        repository.save(wordEntity);
    }
}
