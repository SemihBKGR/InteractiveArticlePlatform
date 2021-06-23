package com.smh.InteractiveArticlePlatformWebService.article;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ArticleRepository extends CrudRepository<Article,Integer> {

    @Query(value="SELECT * FROM articles WHERE title LIKE %?1%",nativeQuery = true)
    List<Article> searchArticleByTitle(String title);

}
