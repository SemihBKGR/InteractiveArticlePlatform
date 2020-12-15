package core;

import core.util.ApiResponse;
import core.util.DataListener;
import core.util.KeyValue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

class DataHandlerTest {

    @BeforeAll
    static void initialize(){
        DataHandler.initialize(DataPolicy.getPolicyBySystemFeatures());
    }

    @Test
    void login(){

        ApiResponse<KeyValue> response=null;

        try {
            response=DataHandler.getDataHandler().login("username","password");
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertNotNull(response);

        System.out.println(response);

        DataHandler.getDataHandler().logout();

    }


    @Test
    void loginAsync(){

        CountDownLatch countDownLatch=new CountDownLatch(1);

        DataHandler.getDataHandler().loginAsync("username", "password", new DataListener<KeyValue>() {
            @Override
            public void onException(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onResult(ApiResponse<KeyValue> response) {
                assertNotNull(response.getData());
                System.out.println(response);
                countDownLatch.countDown();
            }
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }

        DataHandler.getDataHandler().logout();

    }


}