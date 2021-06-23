package com.smh.InteractiveArticlePlatformWebService.chat.message;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MessageServiceImplTest {

    @Autowired
    private MessageService messageService;

    @Test
    @Order(0)
    void save(){

        Message message=new Message();
        message.setMessage("message01");
        message.setSent_at(System.currentTimeMillis());
        message.setArticle(1);
        message.setSender(1);
        message.setReceiver(2);

        messageService.save(message);

    }


    @Test
    @Order(1)
    void get(){
        assertEquals(messageService.findByReceiverId(2).size(),1);
    }

    @Test
    @Order(2)
    void delete(){
        messageService.deleteByReceiverId(2);
    }

    @Test
    @Order(3)
    void getAfterDeleted(){
        assertEquals(messageService.findByReceiverId(2).size(),0);
    }


}