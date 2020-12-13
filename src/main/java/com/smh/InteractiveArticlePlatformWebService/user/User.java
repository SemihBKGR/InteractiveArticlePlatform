package com.smh.InteractiveArticlePlatformWebService.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.smh.InteractiveArticlePlatformWebService.article.Article;
import com.smh.InteractiveArticlePlatformWebService.serialization.ArticleSerializer;
import com.smh.InteractiveArticlePlatformWebService.user.information.Information;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "users")
@Entity
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "id")
    private int id;

    @Column(name="username", nullable = false, unique = true)
    private String username;

    @Column(name="email", nullable = false, unique = true)
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
