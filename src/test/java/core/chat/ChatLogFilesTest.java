package core.chat;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ChatLogFilesTest {

    @Test
    void save(){

        Random random=new Random();

        List<ChatMessage> chatMessages= null;
        try {
            chatMessages = ChatLogFiles.getMessages(3);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        for(int i=0;i<10;i++){
            ChatMessage chatMessage=new ChatMessage();
            chatMessage.setSent_at(System.currentTimeMillis());
            chatMessage.setTo_article_id(random.nextInt(5));
            chatMessage.setFrom_user_id(random.nextInt(5));
            chatMessage.setMessage(UUID.randomUUID().toString().substring(0,20));
            chatMessages.add(chatMessage);
        }

        try {
            ChatLogFiles.saveMessages(3,chatMessages);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    void get(){

        try {
            System.out.println(ChatLogFiles.getMessages(3));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


}