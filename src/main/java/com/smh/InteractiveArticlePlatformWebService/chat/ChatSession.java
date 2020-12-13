package com.smh.InteractiveArticlePlatformWebService.chat;

import lombok.Data;

import java.io.Serializable;

@Data
public class ChatSession implements Serializable {

    private String username;
    private String session_id;

}
