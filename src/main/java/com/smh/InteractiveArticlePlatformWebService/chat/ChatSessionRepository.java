package com.smh.InteractiveArticlePlatformWebService.chat;

public interface ChatSessionRepository {

    ChatSession findByUsername(String username);
    void save(ChatSession chatSession);
    void delete(String username);

}
