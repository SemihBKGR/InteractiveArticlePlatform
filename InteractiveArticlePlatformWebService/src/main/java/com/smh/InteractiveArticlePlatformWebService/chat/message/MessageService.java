package com.smh.InteractiveArticlePlatformWebService.chat.message;

import com.smh.InteractiveArticlePlatformWebService.user.User;

import java.util.List;

public interface MessageService {

    void save(Message message);
    List<Message> findByReceiverId(int id);
    void deleteByReceiverId(int id);

}
