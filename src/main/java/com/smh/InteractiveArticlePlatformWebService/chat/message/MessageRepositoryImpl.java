package com.smh.InteractiveArticlePlatformWebService.chat.message;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Objects;

@Repository
public class MessageRepositoryImpl implements MessageRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Message message) {
        Objects.requireNonNull(message);
        entityManager.createNativeQuery
                ("INSERT INTO messages (receiver_id, sender_id, article_id, message, sent_at) VALUES(?,?,?,?,?)")
                .setParameter(1,message.getReceiver_id())
                .setParameter(2,message.getSender_id())
                .setParameter(3,message.getArticle_id())
                .setParameter(4,message.getMessage())
                .setParameter(5,message.getSent_at());
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Message> findByReceiverId(int id) {
        return entityManager.createNativeQuery("SELECT * FROM messages WHERE receiver_id = ?1",Message.class)
                .setParameter(1,id)
                .getResultList();
    }

    @Override
    public void deleteByReceiverId(int id) {
        entityManager.createNativeQuery("DELETE FROM messages WHERE receiver_id = ?1")
                .setParameter(1,id);
    }
}
