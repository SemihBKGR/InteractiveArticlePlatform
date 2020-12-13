package com.smh.InteractiveArticlePlatformWebService.article;

import com.smh.InteractiveArticlePlatformWebService.article.dto.ArticleCreateDto;
import com.smh.InteractiveArticlePlatformWebService.user.User;
import com.smh.InteractiveArticlePlatformWebService.user.UserService;
import com.smh.InteractiveArticlePlatformWebService.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ApiResponse<Article> create(@RequestBody ArticleCreateDto articleCreateDto){
        System.out.println(articleCreateDto);
        User user=userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        Article article=new Article();
        article.setOwner(user);
        article.setTitle(articleCreateDto.getTitle());
        article.set_private(articleCreateDto.is_private());
        articleService.save(article);

        return ApiResponse.createApiResponse(article,"Article created");

    }

    @PostMapping("/get/id/{id}")
    public ApiResponse<Article> getArticleById(@RequestBody int id){
        return ApiResponse.createConditionalApiResponse(articleService.findById(id),
                ()->"Article found, '"+id+"'",
                ()->"Article cannot found with given id, '"+id+"'");
    }


    @PostMapping("/get/title/{title}")
    public ApiResponse<List<Article>> getArticleByTitle(@RequestBody String title){

        return ApiResponse.createConditionalApiResponse(articleService.findByTitle(title),
                ()->"Articles found, '"+title+"'",
                ()->"Articles cannot found with given title, '"+title+"'");

    }



}
