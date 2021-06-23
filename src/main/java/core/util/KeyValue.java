package core.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@JsonFormat
public class KeyValue {

    @JsonProperty(value = "key",required = true)
    private String key;

    @JsonProperty(value = "value",required = true)
    private String value;

}
