package com.smh.InteractiveArticlePlatformWebService.article.comment;

import com.smh.InteractiveArticlePlatformWebService.article.Article;
import com.smh.InteractiveArticlePlatformWebService.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Comment save(Comment comment) {
        Objects.requireNonNull(comment);
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> findByArticle(Article article) {
        Objects.requireNonNull(article);
        return commentRepository.findByArticle(article);
    }

    @Override
    public List<Comment> findByUser(User user) {
        Objects.requireNonNull(user);
        return commentRepository.findByUser(user);
    }

}
