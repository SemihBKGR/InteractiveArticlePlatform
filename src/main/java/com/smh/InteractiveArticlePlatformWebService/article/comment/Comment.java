package com.smh.InteractiveArticlePlatformWebService.article.comment;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.smh.InteractiveArticlePlatformWebService.article.Article;
import com.smh.InteractiveArticlePlatformWebService.serialization.ArticleSerializer;
import com.smh.InteractiveArticlePlatformWebService.serialization.UserSerializer;
import com.smh.InteractiveArticlePlatformWebService.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@Table(name="comments")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @JsonSerialize(using = UserSerializer.class)
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JsonSerialize(using = ArticleSerializer.class)
    @ManyToOne
    @JoinColumn(name="article_id",nullable = false)
    private Article article;

    @Column(name = "content",nullable = false)
    private String content;

    @CreatedDate
    @Column(name="created_at",nullable = false,updatable = false)
    private long created_at;

    @LastModifiedDate
    @Column(name="updated_at")
    private long updated_at;


}
