package core;


import core.cache.CacheService;
import core.chat.ChatListener;
import core.chat.ChatService;
import core.entity.Article;
import core.entity.Comment;
import core.entity.Information;
import core.entity.User;
import core.entity.dto.ArticleCreateDto;
import core.entity.dto.CommentDto;
import core.entity.dto.LoginDto;
import core.entity.dto.RegisterDto;
import core.request.RequestService;
import core.request.TransactionService;
import core.util.ApiResponse;
import core.util.DataListener;
import core.util.KeyValue;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class DataHandler implements Closeable {

    private ExecutorService executorService;

    private final TransactionService transactionService;
    private final RequestService requestService;
    private final CacheService cacheService;
    private final ChatService chatService;

    private final DataPolicy dataPolicy;

    private DataHandler(DataPolicy dataPolicy) {

        this.dataPolicy=dataPolicy;

        executorService= Executors.newFixedThreadPool(dataPolicy.getDataHandlerWorkerThreadCount());

        transactionService=new TransactionService(dataPolicy);
        requestService=new RequestService(dataPolicy);
        cacheService=new CacheService(dataPolicy);
        chatService=new ChatService(dataPolicy);

        log.info("DataHandler initialized.");

    }

    private static volatile DataHandler dataHandler;

    public static void initialize(DataPolicy dataPolicy){
        if(dataHandler!=null){
            log.error("DataHandler cannot be initialize more than one.");
            return;
        }
        if(dataPolicy==null){
            throw new IllegalArgumentException("DataPolicy argument cannot be null.");
        }
        dataHandler=new DataHandler(dataPolicy);
    }

    public static void initialize(){
        initialize(DataPolicy.getPolicyBySystemFeatures());
    }

    public static DataHandler getDataHandler(){
        if(dataHandler==null){
            log.error("DataHandler has not been initialized, it is initialized now by default args");
            initialize();
        }
        return dataHandler;
    }

    public void addHeader(KeyValue keyValue){
        Objects.requireNonNull(keyValue);
        requestService.getHeaders().put(keyValue.getKey(),keyValue.getValue());
    }

    public void addHeader(String key,String value){
        requestService.getHeaders().put(Objects.requireNonNull(key),Objects.requireNonNull(value));
    }

    public String removeHeader(String key){
        return requestService.getHeaders().remove(key);
    }

    public ApiResponse<KeyValue> login(LoginDto loginDto) throws IOException {
        return transactionService.loginControl(Objects.requireNonNull(loginDto));
    }

    public ApiResponse<KeyValue> login(String username,String password) throws IOException {
        return login(new LoginDto(Objects.requireNonNull(username),Objects.requireNonNull(password)));
    }

    public void loginAsync(LoginDto loginDto, DataListener<KeyValue> listener){

        Objects.requireNonNull(listener);

        CompletableFuture
        .supplyAsync(()->{
            listener.onStart();
            try {
                return login(loginDto);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(),e);
            }
        },executorService)
        .thenAccept(listener::onResult)
        .exceptionally(throwable -> {
            listener.onException(throwable);
            return null;
        });

    }

    public void loginAsync(String username,String password,DataListener<KeyValue> listener){
        loginAsync(new LoginDto(username,password),listener);
    }

    public ApiResponse<User> register(RegisterDto registerDto) throws IOException {
        Objects.requireNonNull(registerDto);
        return transactionService.register(registerDto);
    }

    public ApiResponse<User> register(String username,String email,String password) throws IOException {

        return register(new RegisterDto(Objects.requireNonNull(username),
                Objects.requireNonNull(email),Objects.requireNonNull(password)));

    }

    public void registerAsync (RegisterDto registerDto,DataListener<User> listener){

        Objects.requireNonNull(listener);

        CompletableFuture
        .supplyAsync(()->{
            listener.onStart();
            try {
                return register(registerDto);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(),e);
            }
        },executorService)
        .thenAccept(listener::onResult)
        .exceptionally((throwable) -> {
            listener.onException(throwable);
            return null;
        });

    }

    public void registerAsync (String username,String email,String password,DataListener<User> listener) {

        registerAsync(new RegisterDto(username,email,password),listener);

    }

    public ApiResponse<User> getMe() throws IOException {
        return requestService.getMe();
    }

    public void getMeAsync(DataListener<User> listener){

        Objects.requireNonNull(listener);

        CompletableFuture
        .supplyAsync(()->{
            listener.onStart();
            try {
                return requestService.getMe();
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        },executorService)
        .thenAccept(listener::onResult)
        .exceptionally((throwable -> {
            listener.onException(throwable);
            return null;
        }));

    }

    public ApiResponse<User> getUser(int id) throws IOException {
        ApiResponse<User> response=null;
        if(dataPolicy.isCacheEnable()){
            response=cacheService.getUserIfNotExpired(id);
            if(response==null){
                response=requestService.getUser(id);
                cacheService.addUserCache(id,response);
            }
        }else{
            response=requestService.getUser(id);
        }
        return response;
    }

    public void getUserAsync(int id,DataListener<User> listener){
        Objects.requireNonNull(listener);

        CompletableFuture.supplyAsync(()->{

            listener.onStart();
            ApiResponse<User> response=null;

            if(dataPolicy.isCacheEnable()){
                response=cacheService.getUserIfNotExpired(id);
                if(response!=null){
                    listener.onCache();
                }else{
                    try {
                        response=requestService.getUser(id);
                        listener.onRequest();
                        cacheService.addUserCache(id,response);
                    } catch (IOException e) {
                        throw new RuntimeException(e.getMessage(),e);
                    }
                }
            }else{
                try {
                    response= requestService.getUser(id);
                    listener.onRequest();
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage(),e);
                }
            }

            return response;

        },executorService)
        .thenAccept(listener::onResult)
        .exceptionally((throwable)->{
            listener.onException(throwable);
            return null;
        });

    }


    public ApiResponse<Article> getArticle(int id) throws IOException {
        ApiResponse<Article> response=null;

        if(dataPolicy.isCacheEnable()){
            response=cacheService.getArticleIfNotExpired(id);
            if(response==null){
                response=requestService.getArticle(id);
                cacheService.addArticleCache(id,response);
            }
        }else{
            response=requestService.getArticle(id);
        }

        return response;

    }

    public void getArticleAsync(int id,DataListener<Article> listener){

        Objects.requireNonNull(listener);

        CompletableFuture.supplyAsync(()->{

            listener.onStart();
            ApiResponse<Article> response=null;

            if(dataPolicy.isCacheEnable()){
                response=cacheService.getArticleIfNotExpired(id);
                if(response!=null){
                    listener.onCache();
                }else{
                    try {
                        response=requestService.getArticle(id);
                    } catch (IOException e) {
                        throw new RuntimeException(e.getMessage(),e);
                    }
                    listener.onRequest();
                    cacheService.addArticleCache(id,response);
                }
            }else{
                try {
                    response=requestService.getArticle(id);
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage(),e);
                }
            }

            return response;

        },executorService)
        .thenAccept(listener::onResult)
        .exceptionally(throwable -> {
            listener.onException(throwable);
            return null;
        });

    }

    public ApiResponse<Information> informationSave(User user) throws IOException {
        return requestService.saveInformation(user);
    }

    public void informationSaveAsync(User user, DataListener<Information> listener){

        Objects.requireNonNull(listener);

        CompletableFuture.supplyAsync(()->{
            listener.onStart();
            try {
                return requestService.saveInformation(user);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(),e);
            }

        },executorService)
        .thenAccept(listener::onResult)
        .exceptionally((throwable -> {
            listener.onException(throwable);
            return null;
        }));

    }

    public ApiResponse<Article> createArticle(ArticleCreateDto articleCreateDto) throws IOException {
        return requestService.createArticle(articleCreateDto);
    }

    public void createArticleAsync(ArticleCreateDto articleCreateDto,DataListener<Article> listener){

        Objects.requireNonNull(listener);

        CompletableFuture.supplyAsync(()->{
            listener.onStart();
            try {
                return requestService.createArticle(articleCreateDto);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(),e);
            }
        },executorService)
        .thenAccept(listener::onResult)
        .exceptionally(throwable -> {
            listener.onException(throwable);
            return null;
        });

    }

    public ApiResponse<List<User>> searchUser(String text) throws IOException {
        return requestService.searchUser(text);
    }

    public void searchUserAsync(String text,DataListener<List<User>> listener){

        Objects.requireNonNull(listener);

        CompletableFuture.supplyAsync(()->{
            listener.onStart();
            try {
                return requestService.searchUser(text);
            } catch (IOException e) {
                System.out.println(e.getMessage());
                throw new RuntimeException(e.getMessage(),e);
            }
        },executorService)
        .thenAccept(listener::onResult)
        .exceptionally((throwable -> {
            listener.onException(throwable);
            return null;
        }));

    }

    public ApiResponse<List<Article>> searchArticle(String text) throws IOException {
        return requestService.searchArticle(text);
    }

    public void searchArticleAsync(String text,DataListener<List<Article>> listener){

        Objects.requireNonNull(listener);

        CompletableFuture.supplyAsync(()->{
            listener.onStart();
            try {
                return requestService.searchArticle(text);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(),e);
            }
        },executorService)
        .thenAccept(listener::onResult)
        .exceptionally((throwable -> {
            listener.onException(throwable);
            return null;
        }));

    }

    public void connectWebSocketAsync(){
        executorService.execute(()->{
            try {
                String authorization=requestService.getHeaders().get("Authorization");
                int userId = getMe().getData().getId();
                if(authorization!=null){
                    chatService.connectWebSocket(authorization,userId);
                }else{
                    log.warn("Has not been logged in yet, so cannot connect web socket");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void connectChatSocketAsync(ChatListener chatListener){
        executorService.execute(()->{
            try {
                chatService.connectChatChannel(chatListener);
                chatService.loadMessages(requestService.getMessages().getData());
            } catch (InterruptedException | IOException interruptedException) {
                interruptedException.printStackTrace();
            }
        });
    }

    public void addContributorAsync(int articleId,int userId,DataListener<Article> listener){
        Objects.requireNonNull(listener);
        CompletableFuture.supplyAsync(()->{
            listener.onStart();
            try {
                ApiResponse<Article> response=requestService.addContributor(articleId,userId);
                cacheService.addArticleCache(response);
                return response;
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(),e);
            }
        },executorService)
        .thenAccept(listener::onResult)
        .exceptionally((throwable)->{
            listener.onException(throwable);
            return null;
        });
    }

    public void removeContributorAsync(int articleId,int userId,DataListener<Article> listener){
        Objects.requireNonNull(listener);
        CompletableFuture.supplyAsync(()->{
            listener.onStart();
            try {
                ApiResponse<Article> response=requestService.removeContributor(articleId,userId);
                cacheService.addArticleCache(response);
                return response;
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(),e);
            }

        },executorService)
        .thenAccept(listener::onResult)
        .exceptionally((throwable)->{
            listener.onException(throwable);
            return null;
        });
    }

    public void makeCommentAsync(CommentDto commentDto, DataListener<Comment> listener){
        Objects.requireNonNull(listener);
        CompletableFuture.supplyAsync(()->{
            listener.onStart();
            try {
                return requestService.makeComment(commentDto);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(),e);
            }
        },executorService)
        .thenAccept(listener::onResult)
        .exceptionally(throwable -> {
            listener.onException(throwable);
            return null;
        });
    }

    public void getCommentsByArticleAsync(int articleId,DataListener<List<Comment>> listener){
        Objects.requireNonNull(listener);
        CompletableFuture.supplyAsync(()->{
            listener.onStart();
            try {
                return requestService.getCommentsByArticle(articleId);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage(),e);
            }
        },executorService)
        .thenAccept(listener::onResult)
        .exceptionally(throwable ->{
            listener.onException(throwable);
            return null;
        });

    }


    public ChatService getChatService() {
        return chatService;
    }

    @Override
    public void close() throws IOException {
        executorService.shutdown();
        requestService.close();
        transactionService.close();
    }

}
