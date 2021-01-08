package com.smh.InteractiveArticlePlatformWebService.article.rate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smh.InteractiveArticlePlatformWebService.article.Article;
import com.smh.InteractiveArticlePlatformWebService.user.User;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

import javax.persistence.*;

@ConditionalOnBean(name = {"article","user"})
@NoArgsConstructor
@Data
@Table(name="rates")
@Entity
public class Rate {

    @EmbeddedId
    private RateCompositeId id;

    @Column(name="point")
    private int point;

    @JsonIgnore
    public RateCompositeId getId() {
        return id;
    }

    @JsonProperty
    public void setId(RateCompositeId id) {
        this.id = id;
    }


}
