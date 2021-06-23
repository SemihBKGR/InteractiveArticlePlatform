package core.entity.superficial;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonFormat
public class SuperficialUser {

    private int id;
    private String username;
    private String email;
    private int article_count;
    private int contribute_count;

}
