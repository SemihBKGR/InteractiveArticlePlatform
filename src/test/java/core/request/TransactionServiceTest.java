package core.request;

import core.DataPolicy;
import core.entity.dto.LoginDto;
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
            apiResponse=transactionService.login(loginDto);
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertNotNull(apiResponse);

        System.out.println(apiResponse);

    }

}