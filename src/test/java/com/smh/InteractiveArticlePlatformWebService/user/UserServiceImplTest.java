package com.smh.InteractiveArticlePlatformWebService.user;

import com.smh.InteractiveArticlePlatformWebService.user.information.Information;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
        user.setInformation(new Information());
        assertNotNull(userService.save(user));

    }

    @Test
    @Order(1)
    void findById(){
        assertNotNull(userService.findById(user.getId()));
    }

    @Test
    @Order(1)
    void findByUsername(){
        assertNotNull(userService.findByUsername(user.getUsername()));
    }

    @Test
    @Order(1)
    void findByEmail(){
        assertNotNull(userService.findByEmail(user.getEmail()));
    }

    @Test
    @Order(2)
    void deleteById(){
        userService.deleteById(user.getId());
        assertTrue(true);
    }

}