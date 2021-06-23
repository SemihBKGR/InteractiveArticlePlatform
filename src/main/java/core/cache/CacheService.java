package core.cache;

import core.DataPolicy;
import core.entity.Article;
import core.entity.User;
import core.util.ApiResponse;

import java.util.concurrent.ConcurrentHashMap;

public class CacheService {

    private final ConcurrentHashMap<Integer, CacheResponseData<User>> userCacheTable;
    private final ConcurrentHashMap<Integer, CacheResponseData<Article>> articleCacheTable;
    private final ConcurrentHashMap<Integer, CacheResponseData<byte[]>> imageCacheTable;

    public CacheService(DataPolicy dataPolicy) {
        CacheResponseData.setDataPolicy(dataPolicy);
        userCacheTable = new ConcurrentHashMap<>();
        articleCacheTable=new ConcurrentHashMap<>();
        imageCacheTable =new ConcurrentHashMap<>();
    }

    public void addUserCache(ApiResponse<User> userResponse){
        if(userResponse!=null && userResponse.getData()!=null){
            userCacheTable.put(userResponse.getData().getId(), CacheResponseData.create(userResponse,User.class));
        }
    }

    public void addUserCache(int id,ApiResponse<User> userResponse){
        if(userResponse!=null && userResponse.getData()!=null){
            userCacheTable.put(id, CacheResponseData.create(userResponse,User.class));
        }
    }

    public void addArticleCache(ApiResponse<Article> articleResponse){
        if(articleResponse!=null && articleResponse.getData()!=null){
            articleCacheTable.put(articleResponse.getData().getId(), CacheResponseData.create(articleResponse,Article.class));
        }
    }

    public void addArticleCache(int id,ApiResponse<Article> articleResponse){
        if(articleResponse!=null && articleResponse.getData()!=null){
            articleCacheTable.put(id, CacheResponseData.create(articleResponse,Article.class));
        }
    }

    public void addImageCache(int id,ApiResponse<byte[]> byteResponse){
        if(byteResponse !=null && byteResponse.getData()!=null){
            imageCacheTable.put(id,CacheResponseData.create(byteResponse,byte[].class));
        }
    }

    public ApiResponse<User> getUserIfNotExpired(int id){
        CacheResponseData<User> userData=userCacheTable.get(id);
        if(userData!=null && userData.isValid()) {
            return userData.getResponseData();
        }
        userCacheTable.remove(id);
        return null;
    }

    public ApiResponse<Article> getArticleIfNotExpired(int id){
        CacheResponseData<Article> articleData=articleCacheTable.get(id);
        if(articleData!=null && articleData.isValid()){
            return articleData.getResponseData();
        }
        articleCacheTable.remove(id);
        return null;
    }

    public ApiResponse<byte[]> getImageIfNotExpired(int id){
        CacheResponseData<byte[]> imageData=imageCacheTable.get(id);
        if(imageData!=null && imageData.isValid()){
            return imageData.getResponseData();
        }
        imageCacheTable.remove(id);
        return null;
    }

}
