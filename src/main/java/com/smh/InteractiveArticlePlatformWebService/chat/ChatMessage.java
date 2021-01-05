package com.smh.InteractiveArticlePlatformWebService.chat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.smh.InteractiveArticlePlatformWebService.article.Article;
import com.smh.InteractiveArticlePlatformWebService.user.User;
import lombok.Data;

@Data
@JsonFormat
public class ChatMessage {

    private User from_user;
    private int to_article_id;
    private String message;
    private long sent_at;

}
