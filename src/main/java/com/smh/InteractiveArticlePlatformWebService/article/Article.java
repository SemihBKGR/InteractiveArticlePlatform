package com.smh.InteractiveArticlePlatformWebService.article;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.smh.InteractiveArticlePlatformWebService.serialization.UserListSerializer;
import com.smh.InteractiveArticlePlatformWebService.serialization.UserSerializer;
import com.smh.InteractiveArticlePlatformWebService.user.User;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@Table(name="articles")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Article implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="title",nullable = false)
    private String title;

    @Lob
    @Column(name="content")
    private String content;

    @Column(name="is_released")
    @JsonProperty("is_released")
    private boolean is_released;

    @Column(name="is_private")
    @JsonProperty("is_private")
    private boolean is_private;

    @CreatedDate
    @Column(name="created_at", updatable = false)
    private long created_at;

    @LastModifiedDate
    @Column(name="updated_at")
    private long updated_at;

    //TODO Set cascade type
    @ToString.Exclude
    @JsonSerialize(using = UserSerializer.class)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id",updatable = false)
    private User owner;

    //TODO Set cascade type
    //TODO Change information fetch type to lazy.
    @ToString.Exclude
    @JsonSerialize(using = UserListSerializer.class)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="contributor_joins",
            joinColumns = @JoinColumn(name="article_id"),
            inverseJoinColumns = @JoinColumn(name="user_id"))
    private List<User> contributors;

}
