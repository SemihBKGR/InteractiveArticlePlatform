package core.request;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.DataPolicy;
import core.entity.User;
import core.entity.dto.LoginDto;
import core.entity.dto.RegisterDto;
import core.util.ApiResponse;
import core.util.KeyValue;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.Closeable;
import java.io.IOException;

import static core.request.Contracts.*;

@Slf4j
public class TransactionService implements Closeable {

    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public TransactionService(DataPolicy dataPolicy) {

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

    //Return authorization header if login credentials is correct
    public ApiResponse<KeyValue> loginControl(LoginDto loginDto) throws IOException {

        log.info("LoginControl request is sending");

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

        return result;

    }

    //Return registered user register transaction is success
    public ApiResponse<User> register(RegisterDto registerDto) throws IOException {

        log.info("Registered request is sending");

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

        return result;

    }

    @Override
    public void close() throws IOException {
        httpClient.close();
    }

}
