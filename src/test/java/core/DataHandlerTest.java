package core;

import core.entity.User;
import core.entity.dto.RegisterDto;
import core.util.ApiResponse;
import core.util.DataListener;
import core.util.KeyValue;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DataHandlerTest {

    @BeforeAll
    static void initialize(){
        DataHandler.initialize(DataPolicy.getPolicyBySystemFeatures());
    }

    @Test
    @Order(0)
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
    @Order(0)
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

    @Test
    @Order(1)
    void register(){

        ApiResponse<User> response=null;

        try {
            response=DataHandler.getDataHandler().register(createRandomRegisterDto());
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertNotNull(response);

        System.out.println(response);

    }

    @Test
    @Order(3)
    void registerAsync(){

        CountDownLatch countDownLatch=new CountDownLatch(1);

        DataHandler.getDataHandler().registerAsync(createRandomRegisterDto(), new DataListener<>() {

            @Override
            public void onException(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onResult(ApiResponse<User> response) {
                assertNotNull(response.getData());
                System.out.println(response);
                countDownLatch.countDown();
                System.out.println("2222");
            }

        });

        System.out.println("1111");

        try {
            countDownLatch.await();
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }

    }

    RegisterDto createRandomRegisterDto(){

        Random random=new Random();

        return new RegisterDto(
                "newusername"+random.nextInt(1000),
                "newemail"+random.nextInt(1000),
                "newpassword"+random.nextInt(1000));

    }

}