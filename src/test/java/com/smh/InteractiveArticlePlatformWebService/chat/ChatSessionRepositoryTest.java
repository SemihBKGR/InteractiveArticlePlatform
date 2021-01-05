package com.smh.InteractiveArticlePlatformWebService.chat;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ChatSessionRepositoryTest {

    @Autowired
    private ChatSessionRepository chatSessionRepository;

    @Test
    @Order(1)
    void save(){
        ChatSession chatSession=new ChatSession();
        chatSession.setSession_id(UUID.randomUUID().toString());
        chatSession.setUsername("username");
        chatSessionRepository.save(chatSession);
    }

    @Test
    @Order(2)
    void find(){
        System.out.println(chatSessionRepository.findByUsername("username"));
    }

}