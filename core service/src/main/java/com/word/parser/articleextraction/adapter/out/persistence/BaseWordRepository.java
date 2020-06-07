package com.word.parser.articleextraction.adapter.out.persistence;

import com.word.parser.articleextraction.adapter.out.persistence.domain.BaseWordPairDomain;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BaseWordRepository extends MongoRepository<BaseWordPairDomain, String> {
    Optional<BaseWordPairDomain> findByWord(String word);
}
