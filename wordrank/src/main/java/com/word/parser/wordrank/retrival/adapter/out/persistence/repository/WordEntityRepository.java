package com.word.parser.wordrank.retrival.adapter.out.persistence.repository;

import com.word.parser.wordrank.retrival.adapter.out.persistence.model.WordEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WordEntityRepository extends MongoRepository<WordEntity, String> {
    Optional<WordEntity> findByWord(String word);
}
