package core.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
@JsonFormat
public class LoginDto {

    private String username;
    private String password;

}
