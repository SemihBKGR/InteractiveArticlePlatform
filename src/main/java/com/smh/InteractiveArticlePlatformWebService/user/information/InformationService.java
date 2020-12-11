package com.smh.InteractiveArticlePlatformWebService.user.information;

public interface InformationService {

    Information findById(int id);
    Information save(Information information);
    void deleteById(int id);

}
