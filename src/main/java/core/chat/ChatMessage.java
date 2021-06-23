package core.chat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import core.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonFormat
public class ChatMessage implements Serializable {

    private int from_user_id;
    private int to_article_id;
    private String message;
    private long sent_at;

}