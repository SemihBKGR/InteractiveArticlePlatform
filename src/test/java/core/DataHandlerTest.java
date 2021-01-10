package core;

import core.entity.Article;
import core.entity.Comment;
import core.entity.Information;
import core.entity.User;
import core.entity.dto.ArticleCreateDto;
import core.entity.dto.CommentDto;
import core.util.ApiResponse;
import core.util.DataListener;
import core.util.KeyValue;
import org.junit.jupiter.api.*;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DataHandlerTest{


    @BeforeAll
    static void initialize(){
        DataHandler.initialize(DataPolicy.getPolicyBySystemFeatures());
    }

    @Test
    @Order(0)
    void register(){

        ApiResponse<User> response=null;

        try {
            response=DataHandler.getDataHandler().register("username01","email01","password01");
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

        assertNotNull(response);

        System.out.println(response);

    }

    @Test
    @Order(0)
    void registerAsync(){

        CountDownLatch countDownLatch=new CountDownLatch(1);

        DataHandler.getDataHandler().registerAsync("username02","email02","password02", new DataListener<>() {

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
    @Order(1)
    void login(){
        ApiResponse<KeyValue> response=null;
        try {
            response=DataHandler.getDataHandler().login("username01","password01");
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        assertNotNull(response);
    }


    @Test
    @Order(1)
    void loginAsync(){

        CountDownLatch countDownLatch=new CountDownLatch(1);

        DataHandler.getDataHandler().loginAsync("username02", "password02", new DataListener<KeyValue>() {
            @Override
            public void onException(Throwable e) {
                e.printStackTrace();
                fail(e.getMessage());
                countDownLatch.countDown();
            }

            @Override
            public void onResult(ApiResponse<KeyValue> response) {
                assertNotNull(response.getData());
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
        dataHandler.addHeader("Authorization","Basic dXNlcm5hbWUwMTpwYXNzd29yZDAx");

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
        dataHandler.addHeader("Authorization","Basic dXNlcm5hbWUwMTpwYXNzd29yZDAx");

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
        dataHandler.addHeader("Authorization","Basic dXNlcm5hbWUwMTpwYXNzd29yZDAx");

        int id=1;

        ApiResponse<User> data=null;

        try {
            data=dataHandler.getUser(id,false);
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
        dataHandler.addHeader("Authorization","Basic dXNlcm5hbWUwMTpwYXNzd29yZDAx");

        CountDownLatch countDownLatch=new CountDownLatch(1);

        int id=1;

        dataHandler.getUserAsync(id,false,new DataListener<User>() {

            @Override
            public void onStart() {
                System.out.println("Started");
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
    void createArticle(){

        DataHandler dataHandler=DataHandler.getDataHandler();
        dataHandler.addHeader("Authorization","Basic dXNlcm5hbWUwMTpwYXNzd29yZDAx");

        ArticleCreateDto articleCreateDto=new ArticleCreateDto();
        articleCreateDto.setTitle("title01");
        articleCreateDto.set_private(false);
        try {
            dataHandler.createArticle(articleCreateDto);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Test
    @Order(5)
    void getArticle(){

        DataHandler dataHandler=DataHandler.getDataHandler();
        dataHandler.addHeader("Authorization","Basic dXNlcm5hbWUwMTpwYXNzd29yZDAx");
        int id=1;

        ApiResponse<Article> response=null;

        try {
            response=dataHandler.getArticle(id,false);
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }

        assertNotNull(response.getData());
        System.out.println(response);

    }

    @Test
    @Order(5)
    void getArticleAsync(){


        DataHandler dataHandler=DataHandler.getDataHandler();
        dataHandler.addHeader("Authorization","Basic dXNlcm5hbWUwMTpwYXNzd29yZDAx");
        int id=1;

        CountDownLatch countDownLatch=new CountDownLatch(1);

        dataHandler.getArticleAsync(id,false, new DataListener<Article>() {

            @Override
            public void onException(Throwable t) {
                t.printStackTrace();
                fail();
                countDownLatch.countDown();
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
    @Order(6)
    void searchUser(){

        DataHandler dataHandler=DataHandler.getDataHandler();
        dataHandler.addHeader("Authorization","Basic dXNlcm5hbWUwMTpwYXNzd29yZDAx");
        CountDownLatch latch=new CountDownLatch(1);

        AtomicReference<List<User>>asyncUserListReference=new AtomicReference<>();
        List<User> userList=null;

        String text="us";

        dataHandler.searchUserAsync(text, new DataListener<List<User>>() {
            @Override
            public void onStart() {
                System.out.println("started");
            }

            @Override
            public void onException(Throwable t) {
                t.printStackTrace();
                latch.countDown();
                fail(t::getMessage);
            }

            @Override
            public void onResult(ApiResponse<List<User>> response) {
                asyncUserListReference.set(response.getData());
                latch.countDown();
            }
        });

        try {
            userList=dataHandler.searchUser(text).getData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            latch.await();
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }

        assertEquals(asyncUserListReference.get(),userList);

    }


    @Test
    @Order(6)
    void searchArticle(){

        DataHandler dataHandler=DataHandler.getDataHandler();
        dataHandler.addHeader("Authorization","Basic dXNlcm5hbWUwMTpwYXNzd29yZDAx");
        CountDownLatch latch=new CountDownLatch(1);

        String text="ti";

        AtomicReference<List<Article>> asyncArticleList=new AtomicReference<>();
        List<Article> articles=null;

        dataHandler.searchArticleAsync(text, new DataListener<List<Article>>() {
            @Override
            public void onStart() {
                System.out.println("started");
            }

            @Override
            public void onException(Throwable t) {
                t.printStackTrace();
                latch.countDown();
                fail(t::getMessage);
            }

            @Override
            public void onResult(ApiResponse<List<Article>> response) {
                asyncArticleList.set(response.getData());
                latch.countDown();
            }
        });

        try {
            articles=dataHandler.searchArticle(text).getData();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            latch.await();
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }

        assertEquals(asyncArticleList.get(),articles);


    }


    @Test
    @Order(7)
    void makeComment(){

        DataHandler dataHandler=DataHandler.getDataHandler();
        dataHandler.addHeader("Authorization","Basic dXNlcm5hbWUwMTpwYXNzd29yZDAx");

        CountDownLatch countDownLatch=new CountDownLatch(1);

        CommentDto commentDto=new CommentDto();
        commentDto.setArticle_id(1);
        commentDto.setContent("Comment01");

        dataHandler.makeCommentAsync(commentDto, new DataListener<Comment>() {
            @Override
            public void onException(Throwable t) {
                countDownLatch.countDown();
                fail(t.getMessage());
            }

            @Override
            public void onResult(ApiResponse<Comment> response) {
                if(response.isConfirmed()){
                    countDownLatch.countDown();
                    fail(response.getMessage());
                }else{
                    countDownLatch.countDown();
                    System.out.println(response.getData());
                }
            }
        });

        try {
            countDownLatch.await();
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }

    }
    

}