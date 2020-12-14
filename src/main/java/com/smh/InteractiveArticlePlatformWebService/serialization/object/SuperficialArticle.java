package com.smh.InteractiveArticlePlatformWebService.serialization.object;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.smh.InteractiveArticlePlatformWebService.article.Article;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@JsonFormat
public class SuperficialArticle {

    private int id;
    private String title;
    private boolean is_released;
    private boolean is_private;
    private long created_at;
    private long updated_at;

    public SuperficialArticle(Article article){

        id=article.getId();
        title=article.getTitle();
        is_private=article.is_private();
        is_released=article.is_released();
        created_at=article.getCreated_at();
        updated_at=article.getUpdate_at();

    }

}
