package com.smh.InteractiveArticlePlatformWebService.article;

import java.util.List;

public interface ArticleService {

    Article findById(int id);
    List<Article> findByTitle(String title);
    Article save(Article article);
    void delete(Article article);
    List<Article> searchArticleByTitle(String title);

}
