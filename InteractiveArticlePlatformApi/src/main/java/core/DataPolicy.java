package core;


public class DataPolicy {

    private int dataHandlerWorkerThreadCount;

    private boolean cacheEnable;

    private int articleCacheExpirationTimeMs;
    private int userCacheExpirationTimeMs;
    private int imageCacheExpirationTimeMs;

    private PolicyState policyState;

    public enum PolicyState{
        fixed,
        system,
        minimal;
    }

    private DataPolicy(){}

    public static DataPolicy getDefaultFixedPolicy(){
        DataPolicy dataPolicy=new DataPolicy();
        dataPolicy.dataHandlerWorkerThreadCount=32;
        dataPolicy.articleCacheExpirationTimeMs=20_000;
        dataPolicy.userCacheExpirationTimeMs=20_000;
        dataPolicy.imageCacheExpirationTimeMs=120_000;
        dataPolicy.cacheEnable=true;
        dataPolicy.policyState=PolicyState.fixed;
        return dataPolicy;
    }

    public static DataPolicy getPolicyBySystemFeatures(){
        DataPolicy dataPolicy=new DataPolicy();
        dataPolicy.dataHandlerWorkerThreadCount=Runtime.getRuntime().availableProcessors()*8;
        dataPolicy.articleCacheExpirationTimeMs=20_000;
        dataPolicy.userCacheExpirationTimeMs=20_000;
        dataPolicy.imageCacheExpirationTimeMs=120_000;
        dataPolicy.cacheEnable=true;
        dataPolicy.policyState=PolicyState.system;
        return dataPolicy;
    }

    public static DataPolicy createPolicyByFeatures(int dataHandlerWorkerThreadCount, int cacheExpirationTime,boolean cacheEnable) {

        if(dataHandlerWorkerThreadCount <=0 || cacheExpirationTime<=0){
            throw new IllegalArgumentException("DataPolicy's features must not be negative value");
        }

        DataPolicy dataPolicy=new DataPolicy();

        dataPolicy.dataHandlerWorkerThreadCount = dataHandlerWorkerThreadCount;
        dataPolicy.articleCacheExpirationTimeMs=cacheExpirationTime;
        dataPolicy.userCacheExpirationTimeMs=cacheExpirationTime;
        dataPolicy.imageCacheExpirationTimeMs=cacheExpirationTime;
        dataPolicy.cacheEnable=cacheEnable;
        if(dataHandlerWorkerThreadCount>16){
            dataPolicy.policyState=PolicyState.system;
        }else{
            dataPolicy.policyState=PolicyState.minimal;
        }

        return dataPolicy;

    }

    public int getDataHandlerWorkerThreadCount() {
        return dataHandlerWorkerThreadCount;
    }

    public int getArticleCacheExpirationTimeMs() {
        return articleCacheExpirationTimeMs;
    }

    public int getUserCacheExpirationTimeMs() {
        return userCacheExpirationTimeMs;
    }

    public int getImageCacheExpirationTimeMs() {
        return imageCacheExpirationTimeMs;
    }

    public boolean isCacheEnable() {
        return cacheEnable;
    }

    public PolicyState getPolicyState() {
        return policyState;
    }

}

