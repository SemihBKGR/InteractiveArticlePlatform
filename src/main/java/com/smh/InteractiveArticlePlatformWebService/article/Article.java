package com.smh.InteractiveArticlePlatformWebService.article;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.smh.InteractiveArticlePlatformWebService.serialization.UserSerializer;
import com.smh.InteractiveArticlePlatformWebService.user.User;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Table(name="articles")
@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", nullable = false, unique = true)
    private int id;

    @Column(name="title",nullable = false)
    private String title;

    @Column(name="is_released",nullable = false)
    private boolean is_released;

    @Column(name="is_private", nullable = false)
    private boolean is_private;

    @Column(name="created_at", nullable = false)
    private Date created_at;

    @Column(name="updated_at")
    private Timestamp update_at;

    //@JsonSerialize(using = UserSerializer.class)
    //TODO Set cascade type
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    //TODO Set cascade type
    //TODO Change information fetch type to lazy.
    @JsonSerialize(using = UserSerializer.class)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="contributor_joins",
            joinColumns = @JoinColumn(name="article_id"),
            inverseJoinColumns = @JoinColumn(name="user_id"))
    private List<User> contributors;

}
