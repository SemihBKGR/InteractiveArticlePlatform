package com.smh.InteractiveArticlePlatformWebService.chat.message;

import com.smh.InteractiveArticlePlatformWebService.user.User;
import com.smh.InteractiveArticlePlatformWebService.user.UserService;
import com.smh.InteractiveArticlePlatformWebService.util.ApiResponse;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @PostMapping("/get/user/id/{id}")
    public ApiResponse<List<Message>> getMessagesByUserId(@PathVariable("id") int id){

        User user=userService.findById(id);
        Objects.requireNonNull(user);

        List<Message> messages=messageService.findByReceiver(user);

        return ApiResponse.createApiResponse(messages,"Messaged found");

    }

    @PostMapping("/delete/message/id/{id}")
    public void deleteMessageByMessageId(@PathVariable("id") int id){
        messageService.deleteById(id);
    }

}
