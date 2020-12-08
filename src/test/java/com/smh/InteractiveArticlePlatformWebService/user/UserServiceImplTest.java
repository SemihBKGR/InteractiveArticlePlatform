package com.smh.InteractiveArticlePlatformWebService.user;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class UserServiceImplTest {

    @Autowired
    UserService userService;

    User user;

    @Test
    @Order(0)
    void save(){

        user =new User();
        user.setUsername("username");
        user.setEmail("email");
        user.setPassword("password");

        assertEquals(user,userService.save(user));

    }

    @Test
    @Order(1)
    void findById(){
        assertEquals(user,userService.findById(user.getId()));
    }

    @Test
    @Order(1)
    void findByUsername(){
        assertEquals(user,userService.findByUsername(user.getUsername()));
    }

    @Test
    @Order(1)
    void findByEmail(){
        assertEquals(user,userService.findByEmail(user.getEmail()));
    }

    @Test
    @Order(2)
    void deleteById(){
        userService.deleteById(user.getId());
        assertTrue(true);
    }

}