package com.smh.InteractiveArticlePlatformWebService.article;

public interface ArticleService {

    Article findById(int id);
    Article save(Article article);
    void deleteById(int id);

}
