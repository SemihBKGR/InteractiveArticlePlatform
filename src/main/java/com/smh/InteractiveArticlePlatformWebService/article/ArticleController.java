package com.smh.InteractiveArticlePlatformWebService.article;

import com.smh.InteractiveArticlePlatformWebService.user.User;
import com.smh.InteractiveArticlePlatformWebService.user.UserService;
import com.smh.InteractiveArticlePlatformWebService.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public Article create(@RequestBody ArticleCreateDto articleCreateDto){

        User user=userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        Article article=new Article();
        article.setOwner(user);
        article.setTitle(articleCreateDto.getTitle());
        article.set_private(articleCreateDto.is_private());
        article.setContributors(new ArrayList<>());
        articleService.save(article);

        return article;

    }

    @PostMapping("/id/{id}")
    public ApiResponse<Article> getArticleById(@PathVariable("id") int id){
        return ApiResponse.createConditionalApiResponse(articleService.findById(id),
                ()->"Article found, '"+id+"'",
                ()->"Article cannot found with given id, '"+id+"'");
    }


    @PostMapping("/title/{title}")
    public ApiResponse<List<Article>> getArticleByTitle(@PathVariable("/title") String title){

        return ApiResponse.createConditionalApiResponse(articleService.findByTitle(title),
                ()->"Articles found, '"+title+"'",
                ()->"Articles cannot found with given title, '"+title+"'");

    }



}
