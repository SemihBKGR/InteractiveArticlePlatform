package com.smh.InteractiveArticlePlatformWebService.user;

import com.smh.InteractiveArticlePlatformWebService.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/id/{id}")
    public ApiResponse<User> getUserById(@PathVariable("id") int id){
        return ApiResponse.createConditionalApiResponse(
                userService.findById(id),
                ()->"User found, '"+ id +"'",
                ()->"User Not Found with given id, '"+ id +"'");

    }

    @GetMapping("/username/{username}")
    public ApiResponse<User> getUserByUsername(@PathVariable("username")String username){

        return ApiResponse.createConditionalApiResponse(
                userService.findByUsername(username),
                ()->"User found, '"+username+"'",
                ()->"User not found with given username, '"+username+"'");

    }

    @GetMapping("/email/{email}")
    public ApiResponse<User> getUserByByEmail(@PathVariable("email") String email){

        return ApiResponse.createConditionalApiResponse(
                userService.findByEmail(email),
                ()->"User found, '"+email+"'",
                ()->"User not found with given email, '"+email+"'");

    }


}
