package com.smh.InteractiveArticlePlatformWebService.chat.message;

import com.smh.InteractiveArticlePlatformWebService.article.Article;
import com.smh.InteractiveArticlePlatformWebService.chat.ChatMessage;
import com.smh.InteractiveArticlePlatformWebService.user.User;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Objects;

@NoArgsConstructor
@Data
@Table(name="messages")
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="receiver_id",nullable = false, updatable = false)
    private int receiver_id;

    @Column(name="sender_id",nullable = false, updatable = false)
    private int sender_id;

    @Column(name="article_id", nullable = false, updatable = false)
    private int article_id;

    @Column(name = "message",nullable = false, updatable = false)
    private String message;

    @Column(name="sent_at",nullable = false, updatable = false)
    private long sent_at;


}
