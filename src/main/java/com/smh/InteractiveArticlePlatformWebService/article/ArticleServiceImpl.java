package com.smh.InteractiveArticlePlatformWebService.article;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Nullable
    @Override
    public Article findById(int id) {
        return articleRepository.findById(id).orElse(null);
    }

    @Override
    public Article save(Article article){
        Objects.requireNonNull(article);
        return articleRepository.save(article);
    }

    @Override
    public void deleteById(int id) {
        articleRepository.deleteById(id);
    }

}
