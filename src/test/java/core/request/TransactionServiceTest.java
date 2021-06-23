package core.request;

import core.DataPolicy;
import core.entity.User;
import core.entity.dto.LoginDto;
import core.entity.dto.RegisterDto;
import core.util.ApiResponse;
import core.util.KeyValue;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TransactionServiceTest {

    @Test
    void login() {

        TransactionService transactionService=new TransactionService(DataPolicy.getPolicyBySystemFeatures());

        LoginDto loginDto=new LoginDto();
        loginDto.setUsername("username");
        loginDto.setPassword("password");

        ApiResponse<KeyValue> apiResponse=null;

        try {
            apiResponse=transactionService.loginControl(loginDto);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertNotNull(apiResponse);

        System.out.println(apiResponse);
        
    }

    @Test
    void register(){

        TransactionService transactionService=new TransactionService(DataPolicy.getPolicyBySystemFeatures());


        RegisterDto registerDto=new RegisterDto();
        registerDto.setUsername("username");
        registerDto.setEmail("email");
        registerDto.setPassword("password");

        ApiResponse<User> response=null;

        try {
            response=transactionService.register(registerDto);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertNotNull(response);

        System.out.println(response);

    }

}