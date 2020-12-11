package com.smh.InteractiveArticlePlatformWebService.serialization.object;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@NoArgsConstructor
@JsonFormat
public class SuperficialArticle {

    private int id;
    private String title;
    private boolean is_released;
    private boolean is_private;
    private Date created_at;
    private Timestamp updated_at;

}
