package core.util;

import core.util.ApiResponse;

public interface DataListener<T>{

    default void OnStart(){};
    default void onException(Throwable e){};
    default void onCache(){};
    default void onRequest(){};
    default void onResult(ApiResponse<T> response){};

}
