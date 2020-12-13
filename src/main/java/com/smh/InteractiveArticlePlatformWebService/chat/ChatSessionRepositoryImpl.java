package com.smh.InteractiveArticlePlatformWebService.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ChatSessionRepositoryImpl implements ChatSessionRepository {

    @Value("${redis.chat.session.key}")
    private String CHAT_SESSION_KEY;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    private HashOperations<String,String,ChatSession> hashOperations;

    @Lazy
    @Autowired
    public ChatSessionRepositoryImpl(){
        this.hashOperations=redisTemplate.opsForHash();
    }

    @Override
    public ChatSession findByUsername(String username) {
        return hashOperations.get(CHAT_SESSION_KEY,username);
    }

    @Override
    public void save(ChatSession chatSession) {
        hashOperations.put(CHAT_SESSION_KEY,chatSession.getUsername(),chatSession);
    }

    @Override
    public void delete(String username) {
        hashOperations.delete(CHAT_SESSION_KEY,username);
    }

}
