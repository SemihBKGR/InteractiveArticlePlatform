package com.smh.InteractiveArticlePlatformWebService.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.function.Supplier;


@Setter
@Getter
@NoArgsConstructor
@JsonFormat
public class ApiResponse <T> {

    @JsonProperty(value = "status",required = true)
    private int status;

    @JsonProperty(value = "confirmed",required = true)
    private boolean confirmed;

    @JsonProperty(value = "message",required = true)
    private String message;

    @JsonProperty(value = "data")
    private T data;

    @JsonProperty(value = "time",required = true)
    private long time;

    public static <T> ApiResponse<T> createApiResponse(T data){
        ApiResponse<T> response=new ApiResponse<>();
        response.data=data;
        response.confirmed =data!=null;
        response.status= data!=null?HttpStatus.OK.value():HttpStatus.NOT_FOUND.value();
        response.message="No message";
        response.time=System.currentTimeMillis();
        return response;
    }

    public static <T> ApiResponse<T> createApiResponse(T data,String message){
        ApiResponse<T> response=createApiResponse(data);
        response.message=message;
        return response;
    }

    public static <T> ApiResponse<T> createApiResponse(T data,String message,boolean confirmed){
        ApiResponse<T> response=createApiResponse(data);
        response.message=message;
        response.confirmed=confirmed;
        return response;
    }

    public static <T> ApiResponse<T> createConditionalApiResponse
            (T data, String affirmativeMessage,String negativeMessage){
        return data!=null?createApiResponse(data,affirmativeMessage):createApiResponse(data,negativeMessage);
    }

    public static <T> ApiResponse<T> createConditionalApiResponse
            (T data, Supplier<String> affirmativeMessage, Supplier<String> negativeMessage){
        return data!=null?createApiResponse(data,affirmativeMessage.get()):createApiResponse(data,negativeMessage.get());
    }

}
