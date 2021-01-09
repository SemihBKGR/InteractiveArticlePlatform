package com.smh.InteractiveArticlePlatformWebService.serialization.superficial;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.InteractiveArticlePlatformWebService.article.Article;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonFormat
public class SuperficialArticle {

    private int id;
    private String title;
    private int owner_id;
    private String owner_name;
    private String owner_email;
    private int contributor_count;
    private boolean is_released;
    private boolean is_private;
    private long created_at;
    private long updated_at;


    public SuperficialArticle(Article article){

        id=article.getId();
        title=article.getTitle();
        owner_id=article.getOwner().getId();
        owner_name=article.getOwner().getUsername();
        owner_email=article.getOwner().getEmail();
        contributor_count=article.getContributors().size();
        is_private=article.is_private();
        is_released=article.is_released();
        created_at=article.getCreated_at();
        updated_at=article.getUpdated_at();

    }

}
