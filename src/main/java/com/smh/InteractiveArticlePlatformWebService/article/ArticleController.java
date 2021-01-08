package com.smh.InteractiveArticlePlatformWebService.article;

import com.smh.InteractiveArticlePlatformWebService.user.User;
import com.smh.InteractiveArticlePlatformWebService.user.UserService;
import com.smh.InteractiveArticlePlatformWebService.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
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

        return ApiResponse.createApiResponse(article,"Article created successfully");

    }

    @PostMapping("/save")
    public ApiResponse<Article> save(@RequestBody ArticleSaveDto articleSaveDto){
        User user=userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(controlUserHavePermission(user,articleSaveDto.getId())){
            Article article =articleService.findById(articleSaveDto.getId());
            if(article!=null){
                article.setContent(articleSaveDto.getContent());
                articleService.save(article);
                return ApiResponse.createApiResponse(article,"Changes saved successfully");
            }else{
                return ApiResponse.createApiResponse(null,"No such article with given id, id:"+articleSaveDto.getId());
            }
        }else{
            return ApiResponse.createApiResponse(null,"User doesn't have permission to edit article",false);
        }
    }

    @PostMapping("/get/id/{id}")
    public ApiResponse<Article> getArticleById(@PathVariable("id") int id){

        Article article=articleService.findById(id);

        if(article!=null && controlUserHaveAccess(article)){
            return ApiResponse.createApiResponse(article,"Article hjas been found with given id, id:"+id);
        }else{
            return ApiResponse.createApiResponse(null,"Article cannot be found with given id, id:"+id);
        }

    }

    @PostMapping("/contributor/add/{article_id}/{user_id}")
    public ApiResponse<Article> addContributor(@PathVariable("article_id") int articleId,
                                               @PathVariable("user_id") int userId){

        User owner = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        User user=userService.findById(userId);
        Article article=articleService.findById(articleId);

        if(user!=null && article!=null){
            if(user.getId()==owner.getId()){
                return ApiResponse.createApiResponse(article,"Cannot add yourself to article as a contributor",false);
            }else{
                if(controlUserHavePermission(owner,articleId)){
                    if(!controlUserHavePermission(user,articleId)){
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
                return ApiResponse.createApiResponse(article,"Cannot remove yourself from article as a contributor",false);
            }else{
                if(controlUserHavePermission(owner,articleId)){
                    if(controlUserHavePermission(user,articleId)){
                        article.getContributors().remove(user);
                        articleService.save(article);
                        return ApiResponse.createApiResponse(article,"Contributor has been removed from article");
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

        List<Article> searchResult=articleService.searchArticleByTitle(text);
        User user=userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());

        Iterator<Article> searchResultIterator=searchResult.iterator();

        while(searchResultIterator.hasNext()){
            if(!controlUserHaveAccess(user,searchResultIterator.next())){
                searchResultIterator.remove();
            }
        }

        return ApiResponse.createApiResponse(searchResult,"Result found, size:"+searchResult.size());

    }

    private boolean controlUserHaveAccess(Article article){
        if(article.is_private()){
            User user=userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            return controlUserHavePermission(user, article.getId());
        }else{
            return true;
        }
    }

    private boolean controlUserHaveAccess(User user,Article article){
        if(article.is_private()){
            return controlUserHavePermission(user, article.getId());
        }else{
            return true;
        }
    }

    private boolean controlUserHavePermission(User user,int articleId){
        return user.getOwnArticles().stream().map(Article::getId).anyMatch(id->id==articleId) ||
                user.getContributorArticle().stream().map(Article::getId).anyMatch(id->id==articleId);
    }




}
