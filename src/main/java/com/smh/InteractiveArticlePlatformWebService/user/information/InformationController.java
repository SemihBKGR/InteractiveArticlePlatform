package com.smh.InteractiveArticlePlatformWebService.user.information;

import com.smh.InteractiveArticlePlatformWebService.user.User;
import com.smh.InteractiveArticlePlatformWebService.user.UserService;
import com.smh.InteractiveArticlePlatformWebService.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("information")
public class InformationController {

    @Autowired
    private InformationService informationService;

    @Autowired
    private UserService userService;

    @PostMapping("/save")
    public ApiResponse<Information> save(@RequestBody User user){
        return ApiResponse.createApiResponse(informationService.save(user),"Information saved");
    }

    @PostMapping("/image/get/{id}")
    public ApiResponse<byte[]> getImage(@PathVariable("id") int id){
        return ApiResponse.createApiResponse(userService.findById(id).getInformation().getImage(),"Image found");
    }


}
