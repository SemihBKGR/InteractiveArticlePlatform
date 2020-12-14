package com.smh.InteractiveArticlePlatformWebService.article.rate;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
@JsonFormat
public class RateDao {

    private int article_id;
    private int point;

}
