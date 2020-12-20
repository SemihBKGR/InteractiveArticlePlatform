package core.cache;

import core.DataPolicy;
import core.util.ApiResponse;

class CacheResponseData<T> {

    private static DataPolicy dataPolicy;

    static void setDataPolicy(DataPolicy dataPolicy){
        CacheResponseData.dataPolicy=dataPolicy;
    }

    private final ApiResponse<T> responseData;

    private CacheResponseData(ApiResponse<T> responseData) {
        this.responseData=responseData;
    }

    static <T> CacheResponseData<T> create(ApiResponse<T> responseData){
        if(CacheResponseData.dataPolicy==null){
            throw new IllegalStateException("Before create the instance of CacheData, set data policy");
        }
        return new CacheResponseData<T> (responseData);
    }


    ApiResponse<T> getResponseData(){
        return responseData;
    }

    boolean isValid(){
        return System.currentTimeMillis()-responseData.getTime()<
                CacheResponseData.dataPolicy.getCacheExpirationTime();
    }

}
