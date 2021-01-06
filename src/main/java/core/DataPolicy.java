package core;


public class DataPolicy {

    private int dataHandlerWorkerThreadCount;
    private int cacheExpirationTime;
    private boolean cacheEnable;
    private PolicyState policyState;
    private boolean logChatMessages;

    public enum PolicyState{
        fixed,
        system,
        minimal;
    }


    private DataPolicy(){}

    public static DataPolicy getDefaultFixedPolicy(){
        DataPolicy dataPolicy=new DataPolicy();
        dataPolicy.dataHandlerWorkerThreadCount=16;
        dataPolicy.cacheExpirationTime=10_000;
        dataPolicy.cacheEnable=true;
        dataPolicy.policyState=PolicyState.fixed;
        dataPolicy.logChatMessages=true;
        return dataPolicy;
    }

    public static DataPolicy getPolicyBySystemFeatures(){
        DataPolicy dataPolicy=new DataPolicy();
        dataPolicy.dataHandlerWorkerThreadCount =Runtime.getRuntime().availableProcessors()*3;
        dataPolicy.cacheExpirationTime=10_000;
        dataPolicy.cacheEnable=true;
        dataPolicy.policyState=PolicyState.system;
        dataPolicy.logChatMessages=true;
        return dataPolicy;
    }

    public static DataPolicy createPolicyByFeatures(int dataHandlerWorkerThreadCount, int cacheExpirationTime,boolean cacheEnable) {

        if(dataHandlerWorkerThreadCount <=0 || cacheExpirationTime<=0){
            throw new IllegalArgumentException("DataPolicy's features must not be negative value");
        }

        DataPolicy dataPolicy=new DataPolicy();

        dataPolicy.dataHandlerWorkerThreadCount = dataHandlerWorkerThreadCount;
        dataPolicy.cacheExpirationTime = cacheExpirationTime;
        dataPolicy.cacheEnable=cacheEnable;
        dataPolicy.logChatMessages=true;
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

    public int getCacheExpirationTime() {
        return cacheExpirationTime;
    }

    public boolean isCacheEnable() {
        return cacheEnable;
    }

    public PolicyState getPolicyState() {
        return policyState;
    }

}

