package core.request;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.DataPolicy;
import core.chat.Message;
import core.entity.Article;
import core.entity.Comment;
import core.entity.Information;
import core.entity.User;
import core.entity.dto.ArticleCreateDto;
import core.entity.dto.ArticleSaveDto;
import core.entity.dto.CommentDto;
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
import java.util.ArrayList;
import java.util.List;
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
        return sendPostRequest(INFORMATION_SAVE_URL,true,information,Information.class);
    }

    public ApiResponse<Article> createArticle(ArticleCreateDto articleCreateDto) throws IOException {
        log.info("ArticleCreate request is sending");
        Objects.requireNonNull(articleCreateDto);
        return sendPostRequest(ARTICLE_CREATE_URL,true,articleCreateDto,Article.class);
    }

    public ApiResponse<List<User>> searchUser(String text) throws IOException {
        log.info("UserSearch request is sending");
        Objects.requireNonNull(text);
        return sendPostRequestList(concatUrlVariable(USER_SEARCH_URL,text),true,null,User.class);
    }

    public ApiResponse<List<Article>> searchArticle(String text) throws IOException {
        log.info("ArticleSearch request is sending");
        Objects.requireNonNull(text);
        return sendPostRequestList(concatUrlVariable(ARTICLE_SEARCH_URL,text),true,null,Article.class);
    }

    public ApiResponse<List<Message>> getMessages() throws IOException {
        log.info("GetMessages request is sending");
        return sendPostRequestList(MESSAGE_GET_URL,true,null,Message.class);
    }

    public ApiResponse<Article> addContributor(int articleId,int userId) throws IOException {
        log.info("AddContributor request is sending");
        return sendPostRequest(concatUrlVariable(ARTICLE_ADD_CONTRIBUTOR_URL,articleId,userId),true,null,Article.class);
    }

    public ApiResponse<Article> removeContributor(int articleId,int userId) throws IOException {
        log.info("RemoveContributor request is sending");
        return sendPostRequest(concatUrlVariable(ARTICLE_REMOVE_CONTRIBUTOR_URL,articleId,userId),true,null,Article.class);
    }

    public ApiResponse<Comment> makeComment(CommentDto commentDto) throws IOException {
        Objects.requireNonNull(commentDto);
        log.info("MakeComment request is sending");
        return sendPostRequest(COMMENT_MAKE_URL,true,commentDto,Comment.class);
    }

    public ApiResponse<List<Comment>> getCommentsByArticle(int articleId) throws IOException {
        log.info("GetComments request is sending");
        return sendPostRequestList(concatUrlVariable(COMMENT_GET_BY_ARTICLE_URL,articleId),true,null,Comment.class);
    }

    public ApiResponse<byte[]> getImageByUserId(int userId) throws IOException {
        return sendPostRequest(concatUrlVariable(INFORMATION_GET_IMAGE_URL,userId),true,null,byte[].class);
    }

    public ApiResponse<Article> saveArticle(ArticleSaveDto articleSaveDto) throws IOException {
        Objects.requireNonNull(articleSaveDto);
        return sendPostRequest(ARTICLE_SAVE_URL,true,articleSaveDto,Article.class);
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
        result.setData(objectMapper.convertValue(result.getData(),type));
        response.close();
        return result;

    }


    private <T> ApiResponse<List<T>> sendPostRequestList(String url,boolean loadHeaders,Object body, Class<T> type) throws IOException {

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
        ApiResponse<List<T>> result=objectMapper.readValue(EntityUtils.toString(response.getEntity()),new TypeReference<ApiResponse<List<T>>>(){});
        System.out.println(result);
        ArrayList<T> arrayList=new ArrayList<>();
        for(int i=0;i<result.getData().size();i++){
            arrayList.add(i,objectMapper.convertValue(result.getData().get(i),type));
        }
        result.setData(arrayList);
        response.close();
        return result;

    }

    private static String concatUrlVariable(String url,Object variable){
        return url+"/"+variable;
    }

    private static String concatUrlVariable(String url,Object ... variables){
        for(Object variable:variables){
            url+="/"+variable;
        }
        return url;
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
