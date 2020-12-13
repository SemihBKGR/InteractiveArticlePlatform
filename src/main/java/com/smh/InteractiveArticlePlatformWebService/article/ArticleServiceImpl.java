package com.smh.InteractiveArticlePlatformWebService.article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Cacheable("article")
    @Nullable
    @Override
    public Article findById(int id) {
        return articleRepository.findById(id).orElse(null);
    }

    @Cacheable("article")
    @Override
    public List<Article> findByTitle(String title) {
        Objects.requireNonNull(title);
        return articleRepository.findByTitle(title);
    }


    @Caching(evict =
                {@CacheEvict(value = "article",key="#article.id"),
                 @CacheEvict(value = "article",key="#article.title")},
             put =
                {@CachePut(value = "article",key = "#article.id"),
                 @CachePut(value = "article",key = "#article.title")}
             )
    @Override
    public Article save(Article article){
        Objects.requireNonNull(article);
        return articleRepository.save(article);
    }

    @Caching(evict = {
        @CacheEvict(value = "article",key="#article.id"),
        @CacheEvict(value = "article",key="#article.title")})
    @Override
    public void delete(Article article) {
        articleRepository.delete(article);
    }

}
