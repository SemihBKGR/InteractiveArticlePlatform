package com.smh.InteractiveArticlePlatformWebService.article.comment;

import com.smh.InteractiveArticlePlatformWebService.article.Article;
import com.smh.InteractiveArticlePlatformWebService.article.ArticleService;
import com.smh.InteractiveArticlePlatformWebService.user.User;
import com.smh.InteractiveArticlePlatformWebService.user.UserService;
import com.smh.InteractiveArticlePlatformWebService.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @PostMapping("/make")
    public ApiResponse<Comment> makeComment(@RequestBody CommentDto commentDto){

        User user=userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Article article=articleService.findById(commentDto.getArticle_id());

        Objects.requireNonNull(user);
        Objects.requireNonNull(article);

        Comment comment=new Comment();
        comment.setUser(user);
        comment.setArticle(article);
        comment.setContent(commentDto.getContent());

        commentService.save(comment);

        return ApiResponse.createApiResponse(comment,"Comment made",true);

    }

    @PostMapping("/get/article/{id}")
    public ApiResponse<List<Comment>> getCommentByArticle(@PathVariable("id") int id){

        Article article=articleService.findById(id);
        Objects.requireNonNull(article);
        return ApiResponse.createApiResponse(commentService.findByArticle(article),"Comments found");

    }

    @PostMapping("/get/me")
    public ApiResponse<List<Comment>> getCommentByUser(){

        User user=userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Objects.requireNonNull(user);
        return ApiResponse.createApiResponse(commentService.findByUser(user),"Comments found");

    }


}
