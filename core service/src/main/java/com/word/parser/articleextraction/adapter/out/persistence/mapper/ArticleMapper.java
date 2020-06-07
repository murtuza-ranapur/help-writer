package com.word.parser.articleextraction.adapter.out.persistence.mapper;

import com.word.parser.articleextraction.Article;
import com.word.parser.articleextraction.Sentence;
import com.word.parser.articleextraction.adapter.out.persistence.domain.ArticleDomain;
import com.word.parser.articleextraction.adapter.out.persistence.domain.SentenceDomain;
import org.mapstruct.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring", uses = SentenceMapper.class)
public interface ArticleMapper {
    ArticleDomain toDomain(Article article);
    Article toModel(ArticleDomain articleDomain);

    default Map<String, List<Sentence>> mapModel(Map<String, List<SentenceDomain>> map, SentenceMapper mapper){
        Map<String, List<Sentence>> modelMap = new HashMap<>();
        for (Map.Entry<String, List<SentenceDomain>> stringListEntry : map.entrySet()) {
            modelMap.put(stringListEntry.getKey(), mapper.toModel(stringListEntry.getValue()));
        }
        return modelMap;
    }

    default Map<String, List<SentenceDomain>> mapDomain(Map<String, List<Sentence>> map, SentenceMapper mapper){
        Map<String, List<SentenceDomain>> modelMap = new HashMap<>();
        for (Map.Entry<String, List<Sentence>> stringListEntry : map.entrySet()) {
            modelMap.put(stringListEntry.getKey(), mapper.toDomain(stringListEntry.getValue()));
        }
        return modelMap;
    }
}
