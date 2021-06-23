package com.smh.InteractiveArticlePlatformWebService.user;

import com.smh.InteractiveArticlePlatformWebService.article.Article;
import com.smh.InteractiveArticlePlatformWebService.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/get/me")
    public ApiResponse<User> getUserMe(){
        User user=userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return ApiResponse.createApiResponse(user,"Me found");
    }

    @PostMapping("/get/id/{id}")
    public ApiResponse<User> getUserById(@PathVariable("id") int id){
        User foundUser=userService.findById(id);
        if(foundUser!=null){
            if (!foundUser.getUsername().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
                User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
                filterArticles(foundUser.getOwnArticles(), user);
                filterArticles(foundUser.getContributorArticle(), user);
            }
            return ApiResponse.createApiResponse(foundUser,"User found with given id, id:"+id);
        }else{
            return ApiResponse.createApiResponse(null,"User cannot be found with given id, id:"+id);
        }
    }

    @PostMapping("/get/username/{username}")
    public ApiResponse<User> getUserByUsername(@PathVariable("username")String username){
        User foundUser=userService.findByUsername(username);
        if(foundUser!=null){
            if (!foundUser.getUsername().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
                User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
                filterArticles(foundUser.getOwnArticles(), user);
                filterArticles(foundUser.getContributorArticle(), user);
            }
            return ApiResponse.createApiResponse(foundUser,"User found with given username, username:"+username);
        }else{
            return ApiResponse.createApiResponse(null,"User cannot be found with given username, username:"+username);
        }
    }

    @PostMapping("/get/email/{email}")
    public ApiResponse<User> getUserByByEmail(@PathVariable("email") String email){
        User foundUser=userService.findByEmail(email);
        if(foundUser!=null){
            if (!foundUser.getUsername().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
                User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
                filterArticles(foundUser.getOwnArticles(), user);
                filterArticles(foundUser.getContributorArticle(), user);
            }
            return ApiResponse.createApiResponse(foundUser,"User found with given email, email:"+email);
        }else{
            return ApiResponse.createApiResponse(null,"User cannot be found with given email, email:"+email);
        }
    }

    @PostMapping("/search/{text}")
    public ApiResponse<List<User>> searchUser(@PathVariable("text") String text){
        List<User> searchResult=userService.searchUser(text);
        User user=userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        searchResult.forEach(searchUser-> {
            filterArticles(searchUser.getOwnArticles(), user);
            filterArticles(searchUser.getContributorArticle(), user);
        });
        return ApiResponse.createApiResponse(searchResult,"Search result");
    }

    private void filterArticles(List<Article> articles,User user){
        Iterator<Article> articleIterator=articles.iterator();
        while(articleIterator.hasNext()){
            Article article=articleIterator.next();
            if(!article.is_private()||
                user.getOwnArticles().stream().map(Article::getId).anyMatch(id->article.getId()==id) ||
                user.getContributorArticle().stream().map(Article::getId).anyMatch(id->article.getId()==id)){
                continue;
            }
            articleIterator.remove();
        }
    }

}
