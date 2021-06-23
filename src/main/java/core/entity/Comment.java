package core.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import core.entity.superficial.SuperficialArticle;
import core.entity.superficial.SuperficialUser;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonFormat
public class Comment {

    private int id;
    private SuperficialUser user;
    private SuperficialArticle article;
    private String content;
    private long created_at;
    private long updated_at;

}
