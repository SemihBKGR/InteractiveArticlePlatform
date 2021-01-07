package core.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonFormat
public class Comment {

    private int id;
    private User user;
    private Article article;
    private String content;
    private long created_at;
    private long updated_at;

}
