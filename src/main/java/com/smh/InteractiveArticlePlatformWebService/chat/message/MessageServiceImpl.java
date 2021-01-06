package com.smh.InteractiveArticlePlatformWebService.chat.message;

import com.smh.InteractiveArticlePlatformWebService.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public void save(Message message) {
        messageRepository.save(message);
    }

    @Override
    public List<Message> findByReceiverId(int id) {
        return messageRepository.findByReceiverId(id);
    }

    @Override
    public void deleteByReceiverId(int id) {
        messageRepository.deleteByReceiverId(id);
    }


}
