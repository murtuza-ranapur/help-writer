package com.word.parser.articleextraction.adapter.out.persistence;

import com.word.parser.articleextraction.Category;
import com.word.parser.articleextraction.adapter.out.persistence.domain.ArticleDomain;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends MongoRepository<ArticleDomain, String> {
    List<ArticleDomain> findAllByCategory(Category category);
    List<ArticleDomain> findAllByCategoryAndIsProcessedFalse(Category category);
    List<ArticleDomain> findAllByIsProcessedFalse();
    List<ArticleDomain> findAllByWebsite(String website);
    Optional<ArticleDomain> findByUrl(String url);
}
