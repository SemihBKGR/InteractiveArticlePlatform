package core.cache;

import core.DataPolicy;
import core.entity.Article;
import core.entity.User;
import core.util.ApiResponse;

import java.util.concurrent.ConcurrentHashMap;

public class CacheService {

    private final ConcurrentHashMap<Integer, CacheResponseData<User>> userCacheTable;
    private final ConcurrentHashMap<Integer, CacheResponseData<Article>> articleCacheTable;

    public CacheService(DataPolicy dataPolicy) {
        CacheResponseData.setDataPolicy(dataPolicy);
        userCacheTable = new ConcurrentHashMap<>();
        articleCacheTable=new ConcurrentHashMap<>();
    }

    public void addUserCache(ApiResponse<User> userResponse){
        if(userResponse!=null && userResponse.getData()!=null){
            userCacheTable.put(userResponse.getData().getId(), CacheResponseData.create(userResponse));
        }
    }

    public void addUserCache(int id,ApiResponse<User> userResponse){
        if(userResponse!=null){
            userCacheTable.put(id, CacheResponseData.create(userResponse));
        }
    }

    public void addArticleCache(ApiResponse<Article> articleResponse){
        if(articleResponse!=null && articleResponse.getData()!=null){
            articleCacheTable.put(articleResponse.getData().getId(), CacheResponseData.create(articleResponse));
        }
    }

    public void addArticleCache(int id,ApiResponse<Article> articleResponse){
        if(articleResponse!=null){
            articleCacheTable.put(id, CacheResponseData.create(articleResponse));
        }
    }

    public ApiResponse<User> getUser(int id){
        return userCacheTable.get(id).getResponseData();
    }

    public ApiResponse<Article> getArticle(int id){
        return articleCacheTable.get(id).getResponseData();
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

    public void clearExpiredUserCache(){
        //TODO clear user
    }

    public void clearExpiredArticleCache(){
        //TODO clear article
    }

}
