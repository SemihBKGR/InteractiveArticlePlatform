package core.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
@JsonFormat
public class ArticleCreateDto {

    private String title;
    private boolean is_private;

}
