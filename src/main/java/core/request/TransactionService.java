package core.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.DataHandler;
import core.DataPolicy;
import core.entity.User;
import core.entity.dto.LoginDto;
import core.entity.dto.RegisterDto;
import core.util.ApiResponse;
import core.util.KeyValue;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.Closeable;
import java.io.IOException;

@Slf4j
public class TransactionService implements Closeable {

    private static final String BASE_URL="http://localhost:8080";
    private static final String LOGIN_URL=BASE_URL+"/loginControl";
    private static final String REGISTER_URL=BASE_URL+"/register";

    private KeyValue authorizationHeader;

    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public TransactionService(DataPolicy dataPolicy) {
        authorizationHeader = null;

        switch (dataPolicy.getPolicyState()){
            case fixed:
                httpClient=HttpClients.createDefault();
                break;
            case minimal:
                httpClient=HttpClients.createMinimal();
                break;
            case system:
                httpClient=HttpClients.createSystem();
                break;
            default:
                httpClient=HttpClients.createDefault();
        }

        objectMapper=new ObjectMapper();

    }

    public ApiResponse<KeyValue> login(LoginDto loginDto) throws IOException {

        if(authorizationHeader!=null){
            throw new IllegalStateException("There is account already logged in.");
        }

        StringEntity entity=new StringEntity(
                objectMapper.writeValueAsString(loginDto),
                ContentType.APPLICATION_JSON);

        HttpPost httpPost=new HttpPost(LOGIN_URL);
        httpPost.setEntity(entity);

        CloseableHttpResponse response=httpClient.execute(httpPost);

        ApiResponse<KeyValue> result=objectMapper.readValue(
                EntityUtils.toString(response.getEntity()),
                new TypeReference<ApiResponse<KeyValue>>(){});

        response.close();

        authorizationHeader=result.getData();

        log.info("Logged in");

        return result;

    }

    public ApiResponse<User> register(RegisterDto registerDto) throws IOException {

        if(authorizationHeader!=null){
            throw new IllegalStateException("There is account already logged in.");
        }

        StringEntity entity=new StringEntity(
                objectMapper.writeValueAsString(registerDto),
                ContentType.APPLICATION_JSON);

        HttpPost httpPost=new HttpPost(REGISTER_URL);
        httpPost.setEntity(entity);

        CloseableHttpResponse response=httpClient.execute(httpPost);

        ApiResponse<User> result=objectMapper.readValue(
                EntityUtils.toString(response.getEntity()),
                new TypeReference<ApiResponse<User>>(){});

        response.close();

        log.info("Registered.");

        return result;
    }

    public void logout(){

        if(authorizationHeader==null){
            throw new IllegalStateException("No account already logged in.");
        }
        log.info("Logged out");
        authorizationHeader=null;

    }

    @Override
    public void close() throws IOException {
        httpClient.close();
    }

}
