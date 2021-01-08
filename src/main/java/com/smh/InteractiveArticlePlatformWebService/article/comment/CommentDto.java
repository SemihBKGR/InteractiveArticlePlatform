package com.smh.InteractiveArticlePlatformWebService.article.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

@Data
@NoArgsConstructor
@JsonFormat
public class CommentDto {

    private int article_id;
    private String content;

}
