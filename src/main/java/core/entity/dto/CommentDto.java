package core.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonFormat
public class CommentDto {

    private int article_id;
    private String content;

}
