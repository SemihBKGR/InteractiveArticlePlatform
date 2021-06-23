package core.entity.superficial;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonFormat
public class SuperficialArticle {

    private int id;
    private String title;
    private int owner_id;
    private String owner_name;
    private String owner_email;
    private int contributor_count;
    private boolean is_released;
    private boolean is_private;
    private long created_at;
    private long updated_at;

}
