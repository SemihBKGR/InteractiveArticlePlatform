package com.smh.InteractiveArticlePlatformWebService.article.rate;

public interface RateService {
    Rate save(Rate rate);
    Rate findById(RateCompositeId id);
    void deleteById(RateCompositeId id);
}
