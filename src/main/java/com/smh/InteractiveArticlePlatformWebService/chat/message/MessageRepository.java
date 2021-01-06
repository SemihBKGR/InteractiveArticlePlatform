package com.smh.InteractiveArticlePlatformWebService.chat.message;

import com.smh.InteractiveArticlePlatformWebService.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepository {

    void save(Message message);
    List<Message> findByReceiverId(int id);
    void deleteByReceiverId(int id);

}
