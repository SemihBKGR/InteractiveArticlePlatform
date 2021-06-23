package com.smh.InteractiveArticlePlatformWebService.chat.message;

import com.smh.InteractiveArticlePlatformWebService.user.User;
import com.smh.InteractiveArticlePlatformWebService.user.UserService;
import com.smh.InteractiveArticlePlatformWebService.util.ApiResponse;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @PostMapping("/get")
    public ApiResponse<List<Message>> getMessages(){

        User user=userService.findByUsername
                (SecurityContextHolder.getContext().getAuthentication().getName());

        List<Message> messages=messageService.findByReceiverId(user.getId());
        messageService.deleteByReceiverId(user.getId());

        return ApiResponse.createApiResponse(messages,"Messages found, size : "+messages.size());

    }

}
