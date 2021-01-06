package core.chat;

import core.DataHandler;
import core.DataPolicy;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

class ChatHandlerTest {

    @Test
    void connect() throws IOException, InterruptedException {

        CountDownLatch countDownLatch=new CountDownLatch(1);


        ChatService chatHandler=new ChatService(DataPolicy.getPolicyBySystemFeatures());
        chatHandler.connectWebSocket("Basic dXNlcm5hbWUwMjpwYXNzd29yZDAy");

        ChatService chatHandler2=new ChatService(DataPolicy.getPolicyBySystemFeatures());
        chatHandler2.connectWebSocket("Basic dXNlcm5hbWUwNDpwYXNzd29yZDA0");

        chatHandler.connectChatChannel(chatMessage -> {
            System.out.println("Message receiver : " + chatMessage);
            countDownLatch.countDown();
        });

        chatHandler2.connectChatChannel(chatMessage -> {
            System.out.println("Message receiver : " + chatMessage);
            countDownLatch.countDown();
        });


        ChatMessage chatMessage=new ChatMessage();

        chatMessage.setFrom_user_id(2);
        chatMessage.setMessage("Message");
        chatMessage.setSent_at(System.currentTimeMillis());
        chatMessage.setTo_article_id(3);

        Thread.sleep(5000);

        chatHandler.sendChatMessage(chatMessage);

        countDownLatch.await();


    }

}