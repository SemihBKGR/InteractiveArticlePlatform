package core.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import core.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.lang.model.type.ArrayType;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonFormat
public class ApiResponse<T> {

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

    public static <T> List<ApiResponse<T>> extractList(ApiResponse<List<T>> response){
        if(response.getData()!=null){
            ArrayList<ApiResponse<T>> result=new ArrayList<>();
            for(T data:response.getData()){
                ApiResponse<T> apiResponse=new ApiResponse<>();
                apiResponse.status=response.getStatus();
                apiResponse.confirmed=response.isConfirmed();
                apiResponse.message=response.getMessage();
                apiResponse.data=data;
                apiResponse.time=response.getTime();
                result.add(apiResponse);
            }
            return result;
        }else{
            return new ArrayList<>();
        }
    }

    public static ApiResponse<byte[]> extractUserToImage(ApiResponse<User> apiResponse){
        ApiResponse<byte[]> response=new ApiResponse<>();
        response.status=apiResponse.getStatus();
        response.confirmed=apiResponse.isConfirmed();
        response.message=apiResponse.getMessage();
        response.data=apiResponse.getData().getInformation().getImage();
        response.time=apiResponse.getTime();
        return response;
    }


}
