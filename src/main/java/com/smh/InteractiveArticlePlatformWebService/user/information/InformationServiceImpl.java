package com.smh.InteractiveArticlePlatformWebService.user.information;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;

import java.util.Objects;

public class InformationServiceImpl implements InformationService {

    @Autowired
    private InformationRepository informationRepository;

    @Nullable
    @Override
    public Information findById(int id) {
        return informationRepository.findById(id).orElse(null);
    }

    @Override
    public Information save(Information information) {
        Objects.requireNonNull(information);
        return informationRepository.save(information);
    }

    @Override
    public void deleteById(int id) {
        informationRepository.deleteById(id);
    }

}
