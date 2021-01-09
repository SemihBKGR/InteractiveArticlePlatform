package core.entity.superficial;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonFormat
public class SuperficialUser {

    @JsonProperty(required = true)
    private int id;

    @JsonProperty(required = true)
    private String username;

    @JsonProperty(required = true)
    private String email;

    @JsonProperty(required = true)
    private int article_count;

    @JsonProperty(required = true)
    private int contribute_count;

}
