package com.smh.InteractiveArticlePlatformWebService.article;

import com.smh.InteractiveArticlePlatformWebService.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class ArticleControllerTest {

    @Autowired
    private ArticleController articleController;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @Test
    void addContributor(){

        Article article=new Article();

        article.setOwner(userService.findById(1));
        article.set_private(false);
        article.setTitle(UUID.randomUUID().toString().substring(0,5));

        articleService.save(article);

        System.out.println(article);

        article.setContributors(new ArrayList<>(Arrays.asList(userService.findByUsername("username02"))));

        articleService.save(article);

        System.out.println(articleService.findById(article.getId()).getContributors().size());

    }


}