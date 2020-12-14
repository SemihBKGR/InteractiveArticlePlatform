package com.smh.InteractiveArticlePlatformWebService.article.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Data
@JsonFormat
public class CommentDto {

    private int article_id;
    private String content;

}
