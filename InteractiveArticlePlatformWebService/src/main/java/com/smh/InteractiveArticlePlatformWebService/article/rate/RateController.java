package com.smh.InteractiveArticlePlatformWebService.article.rate;

import com.smh.InteractiveArticlePlatformWebService.article.Article;
import com.smh.InteractiveArticlePlatformWebService.article.ArticleService;
import com.smh.InteractiveArticlePlatformWebService.user.User;
import com.smh.InteractiveArticlePlatformWebService.user.UserService;
import com.smh.InteractiveArticlePlatformWebService.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/rate")
public class RateController {

    @Autowired
    private RateService rateService;

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @PostMapping("/make")
    public ApiResponse<Rate> makeRate(@RequestBody RateDao rateDao){

        User user=userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Article article=articleService.findById(rateDao.getArticle_id());

        Objects.requireNonNull(user);
        Objects.requireNonNull(article);

        Rate rate=new Rate();

        RateCompositeId rateCompositeId=new RateCompositeId();
        rateCompositeId.setUser(user);
        rateCompositeId.setArticle(article);

        rate.setId(rateCompositeId);
        rate.setPoint(rateDao.getPoint());

        rateService.save(rate);

        return ApiResponse.createApiResponse(rate,"Rate made");

    }

    @PostMapping("/get/article/{id}")
    public ApiResponse<Rate> getRateById(@PathVariable("id")int id){

        User user=userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Article article=articleService.findById(id);

        Objects.requireNonNull(user);
        Objects.requireNonNull(article);

        RateCompositeId rateCompositeId=new RateCompositeId();
        rateCompositeId.setUser(user);
        rateCompositeId.setArticle(article);

        Rate rate=rateService.findById(rateCompositeId);

        return ApiResponse.createApiResponse(rate,"Rate "+rate.getId(),true);

    }


    @PostMapping("/delete/article/{id}")
    public ApiResponse<Void> deleteRate(@PathVariable("id") int id){

        User user=userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Article article=articleService.findById(id);

        Objects.requireNonNull(user);
        Objects.requireNonNull(article);

        RateCompositeId rateCompositeId=new RateCompositeId();
        rateCompositeId.setUser(user);
        rateCompositeId.setArticle(article);

        rateService.deleteById(rateCompositeId);

        return ApiResponse.createApiResponse(null,"rate deleted");

    }


}
