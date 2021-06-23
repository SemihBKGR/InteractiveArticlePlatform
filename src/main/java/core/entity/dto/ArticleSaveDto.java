package core.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonFormat
public class ArticleSaveDto {

    private int id;
    private String content;

}
