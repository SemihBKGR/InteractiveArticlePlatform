package core.util;

import core.util.ApiResponse;

public interface DataListener<T>{

    default void onStart(){};
    default void onException(Throwable t){};
    default void onResult(ApiResponse<T> response){};

}
