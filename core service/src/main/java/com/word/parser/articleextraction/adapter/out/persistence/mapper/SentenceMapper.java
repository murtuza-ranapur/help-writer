package com.word.parser.articleextraction.adapter.out.persistence.mapper;

import com.word.parser.articleextraction.Sentence;
import com.word.parser.articleextraction.adapter.out.persistence.domain.SentenceDomain;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SentenceMapper {
    SentenceDomain toDomain(Sentence sentence);
    List<SentenceDomain> toDomain(List<Sentence> sentence);
    Sentence toModel(SentenceDomain sentenceDomain);
    List<Sentence> toModel(List<SentenceDomain> sentenceDomains);
}
