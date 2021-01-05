package com.smh.InteractiveArticlePlatformWebService.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class ChatSessionEventListener {

    @Autowired
    private ChatSessionRepository chatSessionRepository;

    @EventListener
    private void handleSessionConnected(SessionConnectEvent event){

        System.out.println("-------------------------------------------------------------Session Connected");

        SimpMessageHeaderAccessor headerAccessor=SimpMessageHeaderAccessor.wrap(event.getMessage());
        ChatSession chatSession=new ChatSession();
        chatSession.setUsername(headerAccessor.getUser().getName());
        chatSession.setSession_id(headerAccessor.getSessionId());
        System.out.println("------------------------------------------------------"+chatSession);
        chatSessionRepository.save(chatSession);

    }

    @EventListener
    private void handleSessionDisconnected(SessionDisconnectEvent event){
        SimpMessageHeaderAccessor headerAccessor=SimpMessageHeaderAccessor.wrap(event.getMessage());
        chatSessionRepository.delete(headerAccessor.getUser().getName());
    }


}
