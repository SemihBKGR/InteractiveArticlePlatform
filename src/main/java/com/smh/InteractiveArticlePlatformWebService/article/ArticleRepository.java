package com.smh.InteractiveArticlePlatformWebService.article;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArticleRepository extends CrudRepository<Article,Integer> {

    List<Article> findByTitle(String title);

}
