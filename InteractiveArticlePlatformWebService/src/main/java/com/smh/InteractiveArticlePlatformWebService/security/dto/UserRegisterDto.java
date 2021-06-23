package com.smh.InteractiveArticlePlatformWebService.security.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

@Data
@JsonFormat
public class UserRegisterDto {

    private String username;
    private String email;
    private String password;

}
