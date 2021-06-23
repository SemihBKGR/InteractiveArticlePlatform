package com.smh.InteractiveArticlePlatformWebService.chat.message;

import com.smh.InteractiveArticlePlatformWebService.chat.ChatMessage;

import java.util.Objects;

public class Messages {

    public static Message convertChatMessageToMessage(int receiverId, ChatMessage chatMessage){
        Objects.requireNonNull(chatMessage);
        Message message=new Message();
        message.setReceiver(receiverId);
        message.setSender(chatMessage.getFrom_user_id());
        message.setArticle(chatMessage.getTo_article_id());
        message.setMessage(chatMessage.getMessage());
        message.setSent_at(chatMessage.getSent_at());
        return message;
    }


}
