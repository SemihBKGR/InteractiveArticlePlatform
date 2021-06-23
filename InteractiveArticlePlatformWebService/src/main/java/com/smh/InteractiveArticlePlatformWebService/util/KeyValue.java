package com.smh.InteractiveArticlePlatformWebService.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
@JsonFormat
public class KeyValue {

    private String key;
    private String value;

}
