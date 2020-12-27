package com.smh.InteractiveArticlePlatformWebService.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void searchUser() {
        System.out.println(userRepository.searchUser("nam"));
        System.out.println(userRepository.searchUserByEmail("sernam"));
        System.out.println(userRepository.searchUserByEmail("mai"));
    }

}