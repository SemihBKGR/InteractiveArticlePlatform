package com.smh.InteractiveArticlePlatformWebService.article;

import com.smh.InteractiveArticlePlatformWebService.user.User;
import com.smh.InteractiveArticlePlatformWebService.user.UserService;
import com.smh.InteractiveArticlePlatformWebService.user.information.Information;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ArticleServiceImplTest {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    Article article;

    @Test
    @Order(0)
    void save(){

        User user=new User();
        user.setUsername("username");
        user.setEmail("email");
        user.setPassword("password");
        user.setInformation(new Information());

        article =new Article();
        article.setTitle("title");
        article.setOwner(user);
        article.set_private(false);
        article.set_released(false);
        article.setCreated_at(new Date(System.currentTimeMillis()));
        article.setUpdate_at(null);

        userService.save(user);

        assertNotNull(articleService.save(article));

    }

    @Test
    @Order(1)
    void findById(){

        Article article=articleService.findById(this.article.getId());
        assertNotNull(article);

    }

    @Test
    @Order(2)
    void deleteById() {

        articleService.deleteById(article.getId());
        assertTrue(true);

    }

}