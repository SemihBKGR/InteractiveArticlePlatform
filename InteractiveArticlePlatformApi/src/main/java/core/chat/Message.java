package core.chat;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonFormat
public class Message {

    private int id;
    private int receiver;
    private int sender;
    private int article;
    private String message;
    private long sent_at;

}
