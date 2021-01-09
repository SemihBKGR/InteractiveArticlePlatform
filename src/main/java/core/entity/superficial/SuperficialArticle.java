package core.entity.superficial;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonFormat
public class SuperficialArticle {

    @JsonProperty(required = true)
    private int id;

    @JsonProperty(required = true)
    private String title;

    @JsonProperty(required = true)
    private int owner_id;

    @JsonProperty(required = true)
    private String owner_name;

    @JsonProperty(required = true)
    private String owner_email;

    @JsonProperty(required = true)
    private int contributor_count;

    @JsonProperty(value="is_released",required = true)
    private boolean is_released;

    @JsonProperty(value="is_private",required = true)
    private boolean is_private;

    @JsonProperty(required = true)
    private long created_at;

    @JsonProperty
    private long updated_at;

}
