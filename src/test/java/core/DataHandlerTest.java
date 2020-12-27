package core;

import core.entity.Article;
import core.entity.Information;
import core.entity.User;
import core.util.ApiResponse;
import core.util.DataListener;
import core.util.KeyValue;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DataHandlerTest{

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

    }


    @Test
    @Order(0)
    void loginAsync(){

        CountDownLatch countDownLatch=new CountDownLatch(1);

        DataHandler.getDataHandler().loginAsync("username", "password", new DataListener<KeyValue>() {
            @Override
            public void onException(Throwable e) {
                e.printStackTrace();
                fail();
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

    }

    @Test
    @Order(1)
    void register(){

        ApiResponse<User> response=null;

        try {
            response=DataHandler.getDataHandler().register("username","email","password");
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

        assertNotNull(response);

        System.out.println(response);

    }

    @Test
    @Order(1)
    void registerAsync(){

        CountDownLatch countDownLatch=new CountDownLatch(1);

        DataHandler.getDataHandler().registerAsync("username","email","password", new DataListener<>() {

            @Override
            public void onException(Throwable e) {
                e.printStackTrace();
                fail();
            }

            @Override
            public void onResult(ApiResponse<User> response) {
                System.out.println(response);
                assertNotNull(response,()->{
                    countDownLatch.countDown();
                    return "Response is null";
                });
                countDownLatch.countDown();
            }

        });

        try {
            countDownLatch.await();
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }

    }


    @Test
    @Order(2)
    void getMe(){

        DataHandler dataHandler=DataHandler.getDataHandler();
        dataHandler.addHeader("Authorization","Basic dXNlcm5hbWU6cGFzc3dvcmQ=");

        ApiResponse<User> data=null;

        try {
            data=dataHandler.getMe();
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

        assertNotNull(data.getData());
        System.out.println(data);

    }

    @Test
    @Order(2)
    void getMeAsync(){

        DataHandler dataHandler=DataHandler.getDataHandler();
        dataHandler.addHeader("Authorization","Basic dXNlcm5hbWU6cGFzc3dvcmQ=");

        CountDownLatch countDownLatch=new CountDownLatch(1);

        dataHandler.getMeAsync(new DataListener<User>() {
            @Override
            public void onException(Throwable t) {
                t.printStackTrace();
                fail();
                countDownLatch.countDown();
            }

            @Override
            public void onResult(ApiResponse<User> response) {
                System.out.println(response);
                assertNotNull(response.getData(),()->{
                    countDownLatch.countDown();
                    return "Response data is null";
                });
                countDownLatch.countDown();
            }

        });

        try {
            countDownLatch.await();
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }

    }

    @Test
    @Order(3)
    void getUser(){

        DataHandler dataHandler=DataHandler.getDataHandler();
        dataHandler.addHeader("Authorization","Basic dXNlcm5hbWU6cGFzc3dvcmQ=");

        int id=1;

        ApiResponse<User> data=null;

        try {
            data=dataHandler.getUser(id);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

        assertNotNull(data.getData());
        System.out.println(data);

    }

    @Test
    @Order(3)
    void getUserAsync(){

        DataHandler dataHandler=DataHandler.getDataHandler();
        dataHandler.addHeader("Authorization","Basic dXNlcm5hbWU6cGFzc3dvcmQ=");

        CountDownLatch countDownLatch=new CountDownLatch(1);

        int id=1;

        dataHandler.getUserAsync(id,new DataListener<User>() {

            @Override
            public void onStart() {
                System.out.println("Started");
            }

            @Override
            public void onCache() {
                System.out.println("Data came from cache");
            }

            @Override
            public void onRequest() {
                System.out.println("Data came from request");
            }

            @Override
            public void onException(Throwable t) {
                t.printStackTrace();
                fail();
                countDownLatch.countDown();
            }

            @Override
            public void onResult(ApiResponse<User> response) {
                System.out.println(response);
                assertNotNull(response.getData(),()->{
                    countDownLatch.countDown();
                    return "Response data is null";
                });
                countDownLatch.countDown();
            }
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }

    }


    @Test
    @Order(4)
    void getArticle(){

        DataHandler dataHandler=DataHandler.getDataHandler();
        dataHandler.addHeader("Authorization","Basic dXNlcm5hbWU6cGFzc3dvcmQ=");
        int id=1;

        ApiResponse<Article> response=null;

        try {
            response=dataHandler.getArticle(id);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

        assertNotNull(response.getData());
        System.out.println(response);

    }

    @Test
    @Order(4)
    void getArticleAsync(){


        DataHandler dataHandler=DataHandler.getDataHandler();
        dataHandler.addHeader("Authorization","Basic dXNlcm5hbWU6cGFzc3dvcmQ=");
        int id=1;

        CountDownLatch countDownLatch=new CountDownLatch(1);

        dataHandler.getArticleAsync(id, new DataListener<Article>() {

            @Override
            public void onException(Throwable t) {
                t.printStackTrace();
                fail();
                countDownLatch.countDown();
            }

            @Override
            public void onCache() {
                System.out.println("Data came from cache");
            }

            @Override
            public void onRequest() {
                System.out.println("Data came from request");
            }

            @Override
            public void onResult(ApiResponse<Article> response) {

                System.out.println(response);

                assertNotNull(response.getData(),()->{
                    countDownLatch.countDown();
                    return "Response data is null";
                });

                countDownLatch.countDown();

            }
        });


        try {
            countDownLatch.await();
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }


    }


    @Test
    @Order(5)
    void searchUser(){

        DataHandler dataHandler=DataHandler.getDataHandler();

        CountDownLatch latch=new CountDownLatch(1);

        dataHandler.searchUserAsync("acc", new DataListener<List<User>>() {
            @Override
            public void onStart() {
                System.out.println("started");
            }

            @Override
            public void onException(Throwable t) {
                t.printStackTrace();
                System.out.println(t.getMessage());
                latch.countDown();
            }

            @Override
            public void onResult(ApiResponse<List<User>> response) {
                System.out.println(response.getData().size());
                latch.countDown();
            }
        });

        try {
            latch.await();
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }


    }



}