package com.smh.InteractiveArticlePlatformWebService.article.rate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class RateServiceImpl implements RateService {

    @Autowired
    private RateRepository rateRepository;

    @Override
    public Rate save(Rate rate) {
        Objects.requireNonNull(rate);
        return rateRepository.save(rate);
    }

    @Nullable
    @Override
    public Rate findById(RateCompositeId id) {
        Objects.requireNonNull(id);
        return rateRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(RateCompositeId id) {
        Objects.requireNonNull(id);
        rateRepository.deleteById(id);
    }

}
