package com.smh.InteractiveArticlePlatformWebService.article.comment;

import com.smh.InteractiveArticlePlatformWebService.article.Article;
import com.smh.InteractiveArticlePlatformWebService.user.User;

import java.util.List;

public interface CommentService {

    Comment save(Comment comment);
    List<Comment> findByArticle(Article article);
    List<Comment> findByUser(User user);

}
