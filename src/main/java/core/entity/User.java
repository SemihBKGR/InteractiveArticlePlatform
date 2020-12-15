package core.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import core.entity.superficial.SuperficialArticle;
import lombok.Data;

import java.util.List;

@Data
@JsonFormat
public class User {

    @JsonProperty(required = true)
    private int id;

    @JsonProperty(required = true)
    private String username;

    @JsonProperty(required = true)
    private String email;

    @JsonProperty
    private Information information;

    @JsonProperty
    private List<SuperficialArticle> contributorArticle;

    @JsonProperty
    private List<SuperficialArticle> ownArticles;

}
