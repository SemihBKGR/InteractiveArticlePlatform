package core.chat;

import java.util.Objects;

public class Messages {

    public static ChatMessage convertMessageToChatMessage(Message message){
        Objects.requireNonNull(message);
        ChatMessage chatMessage=new ChatMessage();
        chatMessage.setFrom_user_id(message.getSender());
        chatMessage.setTo_article_id(message.getArticle());
        chatMessage.setMessage(message.getMessage());
        chatMessage.setSent_at(message.getSent_at());
        return chatMessage;
    }

}
