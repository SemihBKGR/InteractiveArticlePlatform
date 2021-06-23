package com.smh.InteractiveArticlePlatformWebService.chat.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public void save(Message message) {
        messageRepository.save(message);
    }

    @Override
    public List<Message> findByReceiverId(int id) {
        return messageRepository.findByReceiver(id);
    }

    @Override
    public void deleteByReceiverId(int id) {
        messageRepository.deleteByReceiver(id);
    }


}
