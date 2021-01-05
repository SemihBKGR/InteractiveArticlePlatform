package core.chat;

import core.DataHandler;
import core.DataPolicy;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

class ChatHandlerTest {

    @Test
    void connect() throws IOException, InterruptedException {
        ChatHandler chatHandler=new ChatHandler(DataPolicy.getPolicyBySystemFeatures());
        try {
            chatHandler.connectChatSocket("Basic bmV3YWNjMTpuZXdhY2Mx", new ChatListener() {
                @Override
                public void messageReceiver(ChatMessage chatMessage) {
                    System.out.println("User1 : "+chatHandler);
                }
            });
        } catch (ExecutionException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }


        ChatHandler chatHandler2=new ChatHandler(DataPolicy.getPolicyBySystemFeatures());
        try {
            chatHandler2.connectChatSocket("Basic bmV3YWNjOm5ld2FjYw==", new ChatListener() {
                @Override
                public void messageReceiver(ChatMessage chatMessage) {
                    System.out.println("User2 : "+chatHandler);
                }
            });
        } catch (ExecutionException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }


        ChatMessage chatMessage=new ChatMessage();

        chatMessage.setFrom_user(DataHandler.getDataHandler().getUser(33).getData());
        chatMessage.setMessage("Message");
        chatMessage.setSent_at(System.currentTimeMillis());
        chatMessage.setTo_article_id(353);

        chatHandler.send(chatMessage);

        Thread.sleep(1000);

    }

}