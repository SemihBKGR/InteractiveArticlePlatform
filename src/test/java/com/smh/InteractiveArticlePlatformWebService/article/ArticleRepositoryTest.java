package com.smh.InteractiveArticlePlatformWebService.article;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.Order;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ArticleRepositoryTest {

    @Autowired
    private ArticleRepository articleRepository;

    @Test
    void searchArticleByTitle() {

        String searchText="ar";
        List<Article> articles=articleRepository.searchArticleByTitle(searchText);
        System.out.println("Search result size = "+articles.size());
        articles.forEach(System.out::println);

    }

}