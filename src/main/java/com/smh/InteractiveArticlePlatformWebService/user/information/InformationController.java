package com.smh.InteractiveArticlePlatformWebService.user.information;

import com.smh.InteractiveArticlePlatformWebService.user.User;
import com.smh.InteractiveArticlePlatformWebService.util.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("information")
public class InformationController {

    @Autowired
    private InformationService informationService;

    @PostMapping("/save")
    public ApiResponse<Information> save(@RequestBody User user){
        return ApiResponse.createApiResponse(informationService.save(user),"Information saved");
    }

}
