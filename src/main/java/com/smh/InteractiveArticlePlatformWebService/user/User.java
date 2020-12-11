package com.smh.InteractiveArticlePlatformWebService.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.smh.InteractiveArticlePlatformWebService.article.Article;
import com.smh.InteractiveArticlePlatformWebService.serialization.ArticleSerializer;
import com.smh.InteractiveArticlePlatformWebService.user.information.Information;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id", nullable = false)
    private int id;

    @Column(name="username", nullable = false)
    private String username;

    @Column(name="email", nullable = false)
    private String email;

    @Column(name="password", nullable = false)
    private String password;

    //TODO Change information fetch type to lazy.
    @JoinColumn(name = "information_id",referencedColumnName = "id", nullable = false)
    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private Information information;

    //TODO Set cascade type
    //TODO Change information fetch type to lazy.
    @JsonSerialize(using = ArticleSerializer.class)
    @ManyToMany(mappedBy = "contributors",fetch = FetchType.EAGER)
    private List<Article> contributor_article;

    //TODO Set cascade type
    //TODO Change information fetch type to lazy.
    @JsonSerialize(using = ArticleSerializer.class)
    @OneToMany( mappedBy = "owner",fetch = FetchType.EAGER)
    private List<Article> ownArticles;

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

}
