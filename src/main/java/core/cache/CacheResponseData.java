package core.cache;

import core.DataPolicy;
import core.entity.Article;
import core.entity.User;
import core.util.ApiResponse;

class CacheResponseData<T> {

    private static DataPolicy dataPolicy;

    static void setDataPolicy(DataPolicy dataPolicy){
        CacheResponseData.dataPolicy=dataPolicy;
    }

    private final ApiResponse<T> responseData;
    private final Class<T> clazz;
    private final long expirationTime;

    private CacheResponseData(ApiResponse<T> responseData,Class<T> clazz) {
        this.responseData=responseData;
        this.clazz=clazz;
        if(clazz == Article.class){
            expirationTime=dataPolicy.getArticleCacheExpirationTimeMs();
        }else if(clazz == User.class){
            expirationTime=dataPolicy.getUserCacheExpirationTimeMs();
        }else if(clazz == byte[].class){
            expirationTime=dataPolicy.getImageCacheExpirationTimeMs();
        }else{
            throw new IllegalArgumentException("Class '"+clazz+"' is not supported for caching");
        }
    }

    static <T> CacheResponseData<T> create(ApiResponse<T> responseData, Class<T> clazz){
        if(CacheResponseData.dataPolicy==null){
            throw new IllegalStateException("Before create the instance of CacheData, set data policy");
        }
        return new CacheResponseData<T> (responseData,clazz);
    }

    ApiResponse<T> getResponseData(){
        return responseData;
    }

    boolean isValid(){
        return System.currentTimeMillis()-responseData.getTime()<expirationTime;
    }

}
