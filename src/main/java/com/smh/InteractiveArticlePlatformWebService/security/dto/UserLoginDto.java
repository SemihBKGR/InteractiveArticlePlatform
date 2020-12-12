package com.smh.InteractiveArticlePlatformWebService.security.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
@JsonFormat
public class UserLoginDto {

    private String username;
    private String password;

}
