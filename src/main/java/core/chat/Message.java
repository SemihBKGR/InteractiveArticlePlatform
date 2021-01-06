package core.chat;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonFormat
public class Message {

    private int receiver_id;
    private int sender_id;
    private int article_id;
    private String message;
    private long sent_at;

}
