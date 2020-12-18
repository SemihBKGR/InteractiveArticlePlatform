package core;


import core.entity.User;
import core.entity.dto.LoginDto;
import core.entity.dto.RegisterDto;
import core.request.TransactionService;
import core.util.ApiResponse;
import core.util.DataListener;
import core.util.KeyValue;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class DataHandler {

    private ExecutorService executorService;
    private ConcurrentHashMap<String,String> headers;

    private TransactionService transactionService;

    private DataPolicy dataPolicy;

    private DataHandler(DataPolicy dataPolicy) {

        this.dataPolicy=dataPolicy;

        executorService= Executors.newFixedThreadPool(dataPolicy.getDataHandlerWorkerThreadCount());
        headers= new ConcurrentHashMap<>();

        transactionService=new TransactionService(dataPolicy);

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

    public ApiResponse<KeyValue> login(LoginDto loginDto) throws IOException {
        Objects.requireNonNull(loginDto);
        return transactionService.login(loginDto);
    }

    public ApiResponse<KeyValue> login(String username,String password) throws IOException {

        Objects.requireNonNull(username);
        Objects.requireNonNull(password);

        LoginDto loginDto=new LoginDto(username,password);

        return login(loginDto);

    }

    public void loginAsync(LoginDto loginDto, DataListener<KeyValue> listener){

        Objects.requireNonNull(loginDto);
        Objects.requireNonNull(listener);

        CompletableFuture.supplyAsync(()->{
            try {
                return login(loginDto);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        },executorService).thenAcceptAsync(listener::onResult,executorService)
        .handleAsync((userApiResponse,throwable) -> {
            listener.onException(throwable);
            ApiResponse<KeyValue> response=new ApiResponse<>();
            response.setMessage("Exception occurred");
            return response;
        },executorService).thenAcceptAsync(listener::onResult,executorService);

    }

    public void loginAsync(String username,String password,DataListener<KeyValue> listener){

        Objects.requireNonNull(username);
        Objects.requireNonNull(password);
        Objects.requireNonNull(listener);

        LoginDto loginDto=new LoginDto(username,password);

        loginAsync(loginDto,listener);

    }

    public void logout(){
        transactionService.logout();
    }

    public ApiResponse<User> register(RegisterDto registerDto) throws IOException {
        Objects.requireNonNull(registerDto);
        return transactionService.register(registerDto);
    }

    public ApiResponse<User> register(String username,String email,String password) throws IOException {

        Objects.requireNonNull(username);
        Objects.requireNonNull(email);
        Objects.requireNonNull(password);

        RegisterDto registerDto=new RegisterDto(username,email,password);

        return register(registerDto);

    }

    public void registerAsync (RegisterDto registerDto,DataListener<User> listener){

        CompletableFuture.supplyAsync(()->{
            try {
                return transactionService.register(registerDto);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        },executorService).thenAcceptAsync(listener::onResult,executorService)
        .handleAsync((userApiResponse, throwable) -> {
            listener.onException(throwable);
            ApiResponse<User> response=new ApiResponse<>();
            response.setMessage("Exception occurred");
            return response;
        },executorService).thenAcceptAsync(listener::onResult,executorService);

    }

    public void registerAsync (String username,String email,String password,DataListener<User> listener) {

        Objects.requireNonNull(username);
        Objects.requireNonNull(email);
        Objects.requireNonNull(password);

        RegisterDto registerDto=new RegisterDto(username,email,password);

        registerAsync(registerDto,listener);

    }

    public ApiResponse<User> getMe(){

        ApiResponse<User> response=new ApiResponse<>();

    }

    public void getMeAsync(){


    }




}
