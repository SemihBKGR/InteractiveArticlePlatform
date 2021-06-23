package com.smh.InteractiveArticlePlatformWebService.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void searchUser() {
        String searchText="acc";
        List<User> users=userRepository.searchUser(searchText);
        System.out.println("Search result size = "+users.size());
        users.forEach(System.out::println);
    }

}