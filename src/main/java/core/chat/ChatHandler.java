package core.chat;

import core.DataPolicy;
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

import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class ChatHandler {

    private static final String WS_URL ="http://localhost:8080/ws";
    private static final String LISTEN_END_PATH="/user/queue/search";

    private static final String CHAT_END_PATH ="/chat";

    private DataPolicy dataPolicy;

    public ChatHandler(DataPolicy dataPolicy){
        this.dataPolicy=dataPolicy;
    }

    private StompSession session;

    public void connectChatSocket(String base64Authorization,ChatListener chatListener) throws ExecutionException, InterruptedException, URISyntaxException {

        List<Transport> transportList=new ArrayList<>();
        transportList.add(new WebSocketTransport(new StandardWebSocketClient()));
        SockJsClient sockJsClient=new SockJsClient(transportList);
        WebSocketHttpHeaders webSocketHttpHeaders=new WebSocketHttpHeaders();
        webSocketHttpHeaders.add("Authorization",base64Authorization);
        WebSocketStompClient webSocketStompClient=new WebSocketStompClient(sockJsClient);
        webSocketStompClient.setMessageConverter(new MappingJackson2MessageConverter());
        webSocketStompClient.setTaskScheduler(new ConcurrentTaskScheduler());

        webSocketStompClient.connect(WS_URL,webSocketHttpHeaders, new StompSessionHandlerAdapter() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return ChatMessage.class;
            }
            @Override
            public void afterConnected(StompSession session, StompHeaders connectedHeaders) {

                ChatHandler.this.session=session;

                session.subscribe(LISTEN_END_PATH, new StompFrameHandler() {
                    @Override
                    public Type getPayloadType(StompHeaders stompHeaders) {
                        return ChatMessage.class;
                    }
                    @Override
                    public void handleFrame(StompHeaders stompHeaders, Object o) {
                        chatListener.messageReceiver((ChatMessage)o);
                    }
                });

            }
        });

    }

    public void send(ChatMessage chatMessage){
        while(session==null || !session.isConnected()){

        }
        Objects.requireNonNull(session).send(CHAT_END_PATH,chatMessage);
    }


}
