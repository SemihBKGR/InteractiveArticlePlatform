package core.util;

@FunctionalInterface
public interface ResultListener<T> {

    void onResult(ApiResponse<T> apiResponse);

}
