package com.smh.InteractiveArticlePlatformWebService.security;

import com.smh.InteractiveArticlePlatformWebService.security.dto.UserLoginDto;
import com.smh.InteractiveArticlePlatformWebService.security.dto.UserRegisterDto;
import com.smh.InteractiveArticlePlatformWebService.user.User;
import com.smh.InteractiveArticlePlatformWebService.user.UserService;
import com.smh.InteractiveArticlePlatformWebService.user.information.Information;
import com.smh.InteractiveArticlePlatformWebService.util.ApiResponse;
import com.smh.InteractiveArticlePlatformWebService.util.KeyValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@RestController
public class UserTransactionController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/loginControl")
    public ApiResponse<KeyValue> loginControl(@RequestBody UserLoginDto userLoginDto){

        User user=userService.findByUsername(userLoginDto.getUsername());

        if(user!=null){
            if(passwordEncoder.matches(userLoginDto.getPassword(),user.getPassword())){
                KeyValue keyValue=new KeyValue();
                keyValue.setKey("Authorization");
                keyValue.setValue("Basic "+ generateBase64Encode(userLoginDto.getUsername(),":",userLoginDto.getPassword()));
                return ApiResponse.createApiResponse(keyValue,"Login control is successful");
            }
            return ApiResponse.createApiResponse(null,"Wrong password");
        }

        return ApiResponse.createApiResponse
                (null,"wrong username");

    }

    @PostMapping("/register")
    public ApiResponse<User> register(@RequestBody UserRegisterDto userRegisterDto){

        boolean usernameUnique=userService.findByUsername(userRegisterDto.getUsername())==null;
        boolean emailUnique=userService.findByEmail(userRegisterDto.getEmail())==null;

        if(usernameUnique && emailUnique){
            User user=new User();
            user.setUsername(userRegisterDto.getUsername());
            user.setEmail(userRegisterDto.getEmail());
            user.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
            user.setInformation(new Information());
            userService.save(user);
            return ApiResponse.createApiResponse(user,"Account created");
        }

        StringBuilder stringBuilder=new StringBuilder();

        if(!usernameUnique){
            stringBuilder.append("Username is already taken\n");
        }

        if(!emailUnique){
            stringBuilder.append("Email is already taken");
        }

        return ApiResponse.createApiResponse(null,stringBuilder.toString());

    }


    private String generateBase64Encode(String ... strings){

        StringBuilder stringBuilder=new StringBuilder();
        for (String string : strings) {
            stringBuilder.append(string);
        }
        return new String(Base64.getEncoder().encode(stringBuilder.toString().getBytes()));

    }

}
