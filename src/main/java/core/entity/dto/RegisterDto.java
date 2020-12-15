package core.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonFormat
public class RegisterDto {

    @JsonProperty(value = "username" ,required = true)
    private String username;

    @JsonProperty(value = "email" ,required = true)
    private String email;

    @JsonProperty(value = "password" ,required = true)
    private String password;

}
