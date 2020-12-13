package com.smh.InteractiveArticlePlatformWebService.chat.message;

import com.smh.InteractiveArticlePlatformWebService.user.User;

import java.util.List;

public interface MessageService {

    Message save(Message message);
    List<Message> findByReceiver(User user);
    void deleteById(int id);

}
