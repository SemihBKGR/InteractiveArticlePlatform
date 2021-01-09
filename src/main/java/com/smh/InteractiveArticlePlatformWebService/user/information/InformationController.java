package com.smh.InteractiveArticlePlatformWebService.user.information;

import com.smh.InteractiveArticlePlatformWebService.user.User;
import com.smh.InteractiveArticlePlatformWebService.user.UserService;
import com.smh.InteractiveArticlePlatformWebService.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("information")
public class InformationController {

    @Autowired
    private UserService userService;

    @PostMapping("/save")
    public ApiResponse<Information> save(@RequestBody Information information){
        User user= userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        user.setInformation(information);
        userService.save(user);
        return ApiResponse.createApiResponse(information,"Information saved");
    }

    @PostMapping("/image/get/{id}")
    public ApiResponse<byte[]> getImage(@PathVariable("id") int id){
        return ApiResponse.createApiResponse(userService.findById(id).getInformation().getImage(),"Image found");
    }


}
