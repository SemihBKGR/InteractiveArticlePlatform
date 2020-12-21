package com.smh.InteractiveArticlePlatformWebService.serialization.superficial;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.smh.InteractiveArticlePlatformWebService.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonFormat
public class SuperficialUser {

    private int id;
    private String username;
    private String email;

    public SuperficialUser(User user){

        id=user.getId();
        username=user.getUsername();
        email=user.getEmail();

    }

}
