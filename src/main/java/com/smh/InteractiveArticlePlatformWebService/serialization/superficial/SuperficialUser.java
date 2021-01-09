package com.smh.InteractiveArticlePlatformWebService.serialization.superficial;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.smh.InteractiveArticlePlatformWebService.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonFormat
public class SuperficialUser {

    private int id;
    private String username;
    private String email;
    private int article_count;
    private int contribute_count;

    public SuperficialUser(User user){
        id=user.getId();
        username=user.getUsername();
        email=user.getEmail();
        article_count=user.getOwnArticles().size();
        contribute_count=user.getContributorArticle().size();
    }

}
