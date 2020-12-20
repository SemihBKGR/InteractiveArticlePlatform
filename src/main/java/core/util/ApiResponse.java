package core.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

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


}
