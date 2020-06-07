package com.word.parser.wordrank.retrival.adapter.out.persistence.mapper;

import com.word.parser.wordrank.retrival.adapter.out.persistence.model.WordEntity;
import com.word.parser.wordrank.retrival.domain.Word;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WordDomainMapper {
    Word toDomain(WordEntity wordEntity);
    WordEntity toEntity(Word word);
}
