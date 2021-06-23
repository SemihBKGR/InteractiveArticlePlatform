package com.smh.InteractiveArticlePlatformWebService.chat;

import com.smh.InteractiveArticlePlatformWebService.article.Article;
import com.smh.InteractiveArticlePlatformWebService.article.ArticleService;
import com.smh.InteractiveArticlePlatformWebService.chat.message.Message;
import com.smh.InteractiveArticlePlatformWebService.chat.message.MessageService;
import com.smh.InteractiveArticlePlatformWebService.chat.message.Messages;
import com.smh.InteractiveArticlePlatformWebService.user.User;
import com.smh.InteractiveArticlePlatformWebService.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AliasFor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class ChatMessageController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ChatSessionRepository chatSessionRepository;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private MessageService messageService;

    @MessageMapping("/chat")
    public void send(@Payload ChatMessage chatMessage){

        for(User user:prepareUsers(chatMessage)){
            ChatSession chatSession = chatSessionRepository.findByUsername(user.getUsername());
            if(chatSession!=null){
                System.out.println(chatSession.getSession_id());
                simpMessagingTemplate.convertAndSend("/queue/search-user"+chatSession.getSession_id(), chatMessage);
            }else{
                Message message= Messages.convertChatMessageToMessage(user.getId(),chatMessage);
                messageService.save(message);
            }
        }

    }

    private List<User> prepareUsers(ChatMessage chatMessage){

        Article article=articleService.findById(chatMessage.getTo_article_id());
        List<User> users=new ArrayList<>();
        for(User user:article.getContributors()){
            if(user.getId()!=chatMessage.getFrom_user_id()){
                users.add(user);
            }
        }
        if(article.getOwner().getId()!=chatMessage.getFrom_user_id()){
            users.add(article.getOwner());
        }
        return users;

    }

}

