package core.request;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.DataPolicy;
import core.entity.Article;
import core.entity.Information;
import core.entity.User;
import core.util.ApiResponse;
import lombok.extern.log4j.Log4j;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.Closeable;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
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
        return sendPostRequest(ME_URL,true,null,User.class);
    }

    public ApiResponse<User> getUser(int id) throws IOException {
        log.info("GetUser request is sending");
        return sendPostRequest(concatUrlVariable(USER_GET_URL,id),true,null,User.class);
    }

    public ApiResponse<Article> getArticle(int id) throws IOException {
        log.info("GetArticle request is sending");
        return sendPostRequest(concatUrlVariable(ARTICLE_GET_URL,id),true,null,Article.class);
    }

    public ApiResponse<Information> saveInformation(Information information) throws IOException {

        log.info("SaveInformation request is sending");
        Objects.requireNonNull(information);
        return sendPostRequest(INFORMATION_SAVE,true,information,Information.class);

    }

    private <T> ApiResponse<T> sendPostRequest(String url,boolean loadHeaders,Object body, Class<T> type) throws IOException {

        HttpPost httpPost=new HttpPost(url);
        if(loadHeaders){
            loadHeaders(httpPost);
        }
        if(body!=null){
            StringEntity entity=new StringEntity(
                    objectMapper.writeValueAsString(body),
                    ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
        }
        CloseableHttpResponse response=httpClient.execute(httpPost);
        ApiResponse<T> result=objectMapper.readValue(EntityUtils.toString(response.getEntity()),new TypeReference<ApiResponse<T>>(){});
        result=objectMapper.convertValue(result,new TypeReference<ApiResponse<T>>(){});
        result.setData(objectMapper.convertValue(result.getData(),type));
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
