package com.smh.InteractiveArticlePlatformWebService.article;

import com.smh.InteractiveArticlePlatformWebService.serialization.superficial.SuperficialUser;
import com.smh.InteractiveArticlePlatformWebService.user.User;
import com.smh.InteractiveArticlePlatformWebService.user.UserService;
import com.smh.InteractiveArticlePlatformWebService.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
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

        User user=userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        Article article=new Article();
        article.setOwner(user);
        article.setTitle(articleCreateDto.getTitle());
        article.set_private(articleCreateDto.is_private());
        article.setContributors(new ArrayList<>());
        articleService.save(article);

        return ApiResponse.createApiResponse(article,"Article created");

    }

    @PostMapping("/get/id/{id}")
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


    @PostMapping("/contributor/add/{article_id}/{user_id}")
    public ApiResponse<Article> addContributor(@PathVariable("article_id") int articleId,
                                               @PathVariable("user_id") int userId){

        User owner = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        User user=userService.findById(userId);
        Article article=articleService.findById(articleId);

        if(user!=null && article!=null){
            if(user.getId()==owner.getId()){
                return ApiResponse.createApiResponse(article,"Cannot add yourself as contributor",false);
            }else{
                if(owner.getOwnArticles().stream().map(Article::getId).anyMatch(i->i==articleId)){
                    if(!article.getContributors().stream().map(User::getId).anyMatch(i->i==userId)){
                        article.getContributors().add(user);
                        articleService.save(article);
                        return ApiResponse.createApiResponse(article,"Contributor added");
                    }else{
                        return ApiResponse.createApiResponse(article,"This user is already contributor",false);
                    }
                }else{
                    return ApiResponse.createApiResponse(article,"No permission to add contributor",false);
                }
            }
        }else{
            return ApiResponse.createApiResponse(null,"Wrong path parameter",false);
        }


    }


    @PostMapping("/contributor/remove/{article_id}/{user_id}")
    public ApiResponse<Article> removeContributor(@PathVariable("article_id") int articleId,
                                     @PathVariable("user_id") int userId){

        User owner = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        User user=userService.findById(userId);
        Article article=articleService.findById(articleId);


        if(user!=null && article!=null){
            if(user.getId()==owner.getId()){
                return ApiResponse.createApiResponse(article,"Cannot remove yourself as contributor",false);
            }else{
                if(owner.getOwnArticles().stream().map(Article::getId).anyMatch(i->i==articleId)){
                    if(!article.getContributors().stream().map(User::getId).anyMatch(i->i==userId)){
                        article.getContributors().remove(user);
                        articleService.save(article);
                        return ApiResponse.createApiResponse(article,"Contributor removed");
                    }else{
                        return ApiResponse.createApiResponse(article,"This user is not already contributor",false);
                    }
                }else{
                    return ApiResponse.createApiResponse(article,"No permission to add contributor",false);
                }
            }
        }else{
            return ApiResponse.createApiResponse(null,"Wrong path parameter",false);
        }

    }

    @PostMapping("/search/{text}")
    public ApiResponse<List<Article>> searchArticle(@PathVariable("text") String text){
        return ApiResponse.createApiResponse(articleService.searchArticleByTitle(text),"Search result");
    }


}
