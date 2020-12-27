package com.smh.InteractiveArticlePlatformWebService.user;

import com.smh.InteractiveArticlePlatformWebService.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/get/me")
    public ApiResponse<User> getUserMe(){

        User user=userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        Objects.requireNonNull(user);
        return ApiResponse.createApiResponse(user,"Me found");

    }

    @PostMapping("/get/id/{id}")
    public ApiResponse<User> getUserById(@PathVariable("id") int id){
        return ApiResponse.createConditionalApiResponse(
                userService.findById(id),
                ()->"User found, '"+ id +"'",
                ()->"User Not Found with given id, '"+ id +"'");

    }

    @PostMapping("/get/username/{username}")
    public ApiResponse<User> getUserByUsername(@PathVariable("username")String username){

        return ApiResponse.createConditionalApiResponse(
                userService.findByUsername(username),
                ()->"User found, '"+username+"'",
                ()->"User not found with given username, '"+username+"'");

    }

    @PostMapping("/get/email/{email}")
    public ApiResponse<User> getUserByByEmail(@PathVariable("email") String email){

        return ApiResponse.createConditionalApiResponse(
                userService.findByEmail(email),
                ()->"User found, '"+email+"'",
                ()->"User not found with given email, '"+email+"'");

    }

    @PostMapping("/save")
    public ApiResponse<User> saveUser(@RequestBody User user){

        if(userService.findById(user.getId())!=null){
            return ApiResponse.createApiResponse(userService.save(user),"Information saved");
        }

        return ApiResponse.createConditionalApiResponse(null,"","No such used with given id, "+user.getId());

    }

    @PostMapping("/search/{text}")
    public ApiResponse<List<User>> searchUser(@PathVariable("text") String text){
        return ApiResponse.createApiResponse(userService.searchUser(text),"Search result");
    }

}
