package com.word.parser.articleextraction.adapter.out.persistence;

import com.word.parser.articleextraction.Article;
import com.word.parser.articleextraction.Category;
import com.word.parser.articleextraction.adapter.out.persistence.domain.ArticleDomain;
import com.word.parser.articleextraction.adapter.out.persistence.mapper.ArticleMapper;
import com.word.parser.articleextraction.application.port.out.GetArticlePort;
import com.word.parser.articleextraction.application.port.out.SaveLinkDataPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ArticlePersistenceAdapter implements GetArticlePort, SaveLinkDataPort {
    private final ArticleMapper articleMapper;
    private final ArticleRepository repository;

    @Override
    public Optional<Article> getByArticleURL(String url)
    {
        return repository.findByUrl(url).map(articleMapper::toModel);
    }

    @Override
    public Set<Article> getByArticleWebURL(String webUrl) {
        return repository.findAllByWebsite(webUrl).stream().map(articleMapper::toModel).collect(Collectors.toSet());
    }

    @Override
    public Set<Article> getUnprocessedArticleByCategory(Category category) {
        if(Category.ALL == category){
            return repository.findAllByIsProcessedFalse().stream().map(articleMapper::toModel)
                    .collect(Collectors.toSet());
        }else{
            return repository.findAllByCategoryAndIsProcessedFalse(category).stream().map(articleMapper::toModel)
                    .collect(Collectors.toSet());
        }
    }

    @Override
    public Article save(Article article)
    {
        ArticleDomain articleDomain = articleMapper.toDomain(article);
        articleDomain = repository.save(articleDomain);
        return articleMapper.toModel(articleDomain);
    }

    @Override
    public int saveAll(List<Article> articles)
    {
        articles.forEach(this::save);
        return articles.size();
    }
}
