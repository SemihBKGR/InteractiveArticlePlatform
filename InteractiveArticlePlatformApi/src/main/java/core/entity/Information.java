package core.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@JsonFormat
public class Information {

    @JsonProperty(required = true)
    private int id;

    @JsonProperty
    private String name;

    @JsonProperty
    private String surname;

    @JsonProperty
    private byte[] image;

    @JsonProperty
    private String company;

    @JsonProperty
    private String phone;

    @JsonProperty
    private String address;

    @JsonProperty
    private String biography;

    @JsonProperty
    private Date birthday;

    @JsonProperty(required = true)
    private long created_at;

}
