package com.smh.InteractiveArticlePlatformWebService.article.comment;

import com.smh.InteractiveArticlePlatformWebService.article.Article;
import com.smh.InteractiveArticlePlatformWebService.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment,Integer>{

    List<Comment> findByArticle(Article article);
    List<Comment> findByUser(User user);

}
