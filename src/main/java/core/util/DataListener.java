package core.util;

import core.util.ApiResponse;

public interface DataListener<T>{

    default void onStart(){};
    default void onException(Throwable t){};
    default void onCache(){};
    default void onRequest(){};
    default void onResult(ApiResponse<T> response){};

}
