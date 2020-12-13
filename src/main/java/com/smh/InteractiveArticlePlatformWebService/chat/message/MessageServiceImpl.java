package com.smh.InteractiveArticlePlatformWebService.chat.message;

import com.smh.InteractiveArticlePlatformWebService.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public Message save(Message message) {
        Objects.requireNonNull(message);
        return messageRepository.save(message);
    }

    @Override
    public List<Message> findByReceiver(User user) {
        Objects.requireNonNull(user);
        return messageRepository.findByReceiver(user);
    }

    @Override
    public void deleteById(int id) {
        messageRepository.deleteById(id);
    }


}
