package core;


import core.entity.dto.LoginDto;
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
        LoginDto loginDto=new LoginDto();
        loginDto.setUsername(username);
        loginDto.setPassword(password);
        return login(loginDto);
    }

    public void loginAsync(LoginDto loginDto, DataListener<KeyValue> listener){

        Objects.requireNonNull(loginDto);
        Objects.requireNonNull(listener);

        CompletableFuture.supplyAsync(()->{
            try {
                return login(loginDto);
            } catch (IOException e) {
                listener.onException(e);
                throw new RuntimeException(e.getMessage());
            }
        },executorService).exceptionally(throwable -> {
            listener.onException(throwable);
            ApiResponse<KeyValue> response=new ApiResponse<>();
            response.setMessage("Exception occurred");
            return response;
        }).thenAcceptAsync(listener::onResult);

    }

    public void loginAsync(String username,String password,DataListener<KeyValue> listener){

        Objects.requireNonNull(username);
        Objects.requireNonNull(password);
        Objects.requireNonNull(listener);

        LoginDto loginDto=new LoginDto();
        loginDto.setUsername(username);
        loginDto.setPassword(password);

        loginAsync(loginDto,listener);

    }

    public void logout(){
        transactionService.logout();
    }

    public void register(){

    }

}
