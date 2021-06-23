package core.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import core.entity.superficial.SuperficialUser;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonFormat
public class Article {

    @JsonProperty(required = true)
    private int id;

    @JsonProperty(required = true)
    private String title;

    @JsonProperty
    private String content;

    @JsonProperty(required = true)
    private boolean is_released;

    @JsonProperty(required = true)
    private boolean is_private;

    @JsonProperty(required = true)
    private long created_at;

    @JsonProperty
    private long updated_at;

    @JsonProperty
    private SuperficialUser owner;

    @JsonProperty
    private List<SuperficialUser> contributors;

}
