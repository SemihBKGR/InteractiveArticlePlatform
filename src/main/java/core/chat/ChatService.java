package core.chat;

import core.DataPolicy;
import core.request.Contracts;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.annotation.Contract;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class ChatService {

    private static final String WS_URL = Contracts.BASE_URL+"/ws";
    private static final String LISTEN_END_PATH="/user/queue/search";
    private static final String CHAT_END_PATH ="/chat";

    private volatile CountDownLatch socketConnectLatch;

    private final ConcurrentHashMap<Integer,List<ChatMessage>> articleChatMessagesMap;
    private final WeakHashMap<Integer,ChatListener> singleListenerMap;

    private final DataPolicy dataPolicy;

    private ChatListener chatListener;

    private int userId;

    public ChatService(DataPolicy dataPolicy){
        this.dataPolicy=dataPolicy;
        this.userId=-1;
        socketConnectLatch =new CountDownLatch(1);
        articleChatMessagesMap=new ConcurrentHashMap<>();
        singleListenerMap =new WeakHashMap<>();
    }

    private volatile StompSession session;

    public void connectWebSocket(String authorizationToken,int usedId) {

        this.userId=usedId;

        List<Transport> transportList=new ArrayList<>();
        transportList.add(new WebSocketTransport(new StandardWebSocketClient()));
        SockJsClient sockJsClient=new SockJsClient(transportList);
        WebSocketHttpHeaders webSocketHttpHeaders=new WebSocketHttpHeaders();
        webSocketHttpHeaders.add("Authorization",authorizationToken);
        WebSocketStompClient webSocketStompClient=new WebSocketStompClient(sockJsClient);
        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());
        webSocketStompClient.setTaskScheduler(new ConcurrentTaskScheduler());

        webSocketStompClient.connect(WS_URL,webSocketHttpHeaders, new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
                ChatService.this.session=session;
                log.info("Connected to web socket");
                socketConnectLatch.countDown();
            }
        });

    }

    public void disconnectWebSocketAfterConnection() throws InterruptedException {
        socketConnectLatch.await();
        session.disconnect();
        log.info("Disconnected from web socket");
    }

    public void subscribeChatChannelAfterConnection(ChatListener chatListener) throws InterruptedException {
        socketConnectLatch.await();
        subscribeChatChannel(chatListener);
    }

    public void tryConnectChatChannel(ChatListener chatListener){
        if(socketConnectLatch.getCount()==0){
            subscribeChatChannel(chatListener);
        }
    }

    private void subscribeChatChannel(ChatListener chatListener){
        this.chatListener=chatListener;
        session.subscribe(LISTEN_END_PATH, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders stompHeaders) {
                return ChatMessage.class;
            }

            @Override
            public void handleFrame(StompHeaders stompHeaders, Object o) {
                ChatMessage chatMessage=(ChatMessage)o;
                if(chatListener!=null){
                    chatListener.messageReceiver(chatMessage);
                }
                ChatListener singleListener=singleListenerMap.get(chatMessage.getTo_article_id());
                if(singleListener!=null){
                    singleListener.messageReceiver(chatMessage);
                }
                getMessages(chatMessage.getTo_article_id()).add(chatMessage);
                log.info("Message receiver, "+chatMessage.getMessage());
            }
        });
        log.info("Subscribed chat channel");
    }

    //Send chat message if connected socket
    public void sendChatMessage(ChatMessage chatMessage){
        if(socketConnectLatch.getCount()==0){
            session.send(CHAT_END_PATH,chatMessage);
            log.info("Message send, "+chatMessage.getMessage());
            getMessages(chatMessage.getTo_article_id()).add(chatMessage);
            if(chatListener!=null){
                chatListener.messageReceiver(chatMessage);
            }
            ChatListener singleChatListener=singleListenerMap.get(chatMessage.getTo_article_id());
            if(singleChatListener!=null){
                singleChatListener.messageReceiver(chatMessage);
            }
        }else{
            log.warn("Has not been connected web socket channel yet");
        }
    }

    public void loadMessages(List<Message> messages) throws InterruptedException {
        socketConnectLatch.await();
        for(Message message:messages){
            getMessages(message.getArticle()).add(
                    Messages.convertMessageToChatMessage(message));
        }
    }

    public List<ChatMessage> getMessages(int articleId) {

        List<ChatMessage> chatMessages=articleChatMessagesMap.get(articleId);
        if(chatMessages!=null){
            return chatMessages;
        }else{
            try {
                chatMessages=ChatLogFiles.getMessages(articleId,userId);
                articleChatMessagesMap.put(articleId,chatMessages);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return chatMessages;

    }

    public void saveMessagesInLogFiles(){
        for(Map.Entry<Integer,List<ChatMessage>> chatMeIntegerListEntry:articleChatMessagesMap.entrySet()){
            try {
                ChatLogFiles.saveMessages(chatMeIntegerListEntry.getKey(),userId,chatMeIntegerListEntry.getValue());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void clearChatService(){
        articleChatMessagesMap.clear();
        singleListenerMap.clear();
        socketConnectLatch=new CountDownLatch(1);
        userId=-1;
    }

    public void addSingleListener(int articleId,ChatListener chatListener){
        if(chatListener!=null){
            singleListenerMap.put(articleId,chatListener);
        }
    }

    public int getUserId() {
        return userId;
    }

}
