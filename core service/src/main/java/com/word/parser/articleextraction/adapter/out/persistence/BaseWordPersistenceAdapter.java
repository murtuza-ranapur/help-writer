package com.word.parser.articleextraction.adapter.out.persistence;

import com.word.parser.articleextraction.adapter.out.persistence.domain.BaseWordPairDomain;
import com.word.parser.articleextraction.application.port.out.CheckBaseWordPort;
import com.word.parser.articleextraction.application.port.out.SaveBaseWordPort;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class BaseWordPersistenceAdapter implements CheckBaseWordPort, SaveBaseWordPort {
    private final BaseWordRepository baseWordRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public Optional<String> checkIfBaseWordExist(String word) {
        return baseWordRepository.findByWord(word).map(BaseWordPairDomain::getBaseWord);
    }

    @Override
    public void saveBaseWord(String word, String baseword) {
        Query query = new Query();
        query.addCriteria(Criteria.where("word").is(word));
        Update update = new Update();
        update.set("baseWord", baseword);
        mongoTemplate.upsert(query, update, BaseWordPairDomain.class);
    }
}
