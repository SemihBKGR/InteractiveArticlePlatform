package core.chat;

import com.fasterxml.jackson.annotation.JsonFormat;
import core.entity.User;
import lombok.Data;

@Data
@JsonFormat
public class ChatMessage {

    private User from_user;
    private int to_article_id;
    private String message;
    private long sent_at;

}