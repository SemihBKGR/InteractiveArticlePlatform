package core.request;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.DataPolicy;
import core.entity.Article;
import core.entity.User;
import core.util.ApiResponse;
import lombok.extern.log4j.Log4j;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static core.request.Contracts.*;

@Log4j
public class RequestService implements Closeable {

    private final ConcurrentHashMap<String,String> headers;

    private final CloseableHttpClient httpClient;
    private final ObjectMapper objectMapper;

    public RequestService (DataPolicy dataPolicy){

        headers=new ConcurrentHashMap<>();

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

    public ApiResponse<User> getMe() throws IOException {
        log.info("GetMe request is sending");
        return sendPostRequest(ME_URL,true);
    }

    public ApiResponse<User> getUser(int id) throws IOException {
        log.info("GetUser request is sending");
        return sendPostRequest(concatUrlVariable(USER_GET_URL,id),true);
    }

    public ApiResponse<Article> getArticle(int id) throws IOException {
        log.info("GetArticle request is sending");
        return sendPostRequest(concatUrlVariable(ARTICLE_GET_URL,id),true);
    }

    private <T> ApiResponse<T> sendPostRequest(String url,boolean loadHeaders) throws IOException {

        HttpPost httpPost=new HttpPost(url);
        if(loadHeaders){
            loadHeaders(httpPost);
        }
        CloseableHttpResponse response=httpClient.execute(httpPost);
        ApiResponse<T> result=objectMapper.readValue(EntityUtils.toString(response.getEntity()),
                new TypeReference<ApiResponse<T>>(){});
        response.close();
        return result;

    }

    private static String concatUrlVariable(String url,Object variable){
        return url+"/"+variable;
    }

    private void loadHeaders(HttpRequest request){
        for(Map.Entry<String,String> header:headers.entrySet()){
            request.addHeader(header.getKey(),header.getValue());
        }
    }

    public ConcurrentHashMap<String, String> getHeaders() {
        return headers;
    }

    @Override
    public void close() throws IOException {
        httpClient.close();
    }

}
