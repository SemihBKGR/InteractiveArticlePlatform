package com.smh.InteractiveArticlePlatformWebService.user.information;

import com.smh.InteractiveArticlePlatformWebService.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class InformationServiceImpl implements InformationService {

    @Autowired
    private InformationRepository informationRepository;

    @Caching(evict =
        {@CacheEvict(value = "user", key = "#user.id"),
                @CacheEvict(value = "user", key = "#user.username"),
                @CacheEvict(value = "user", key = "#user.email")},
        put =
        {@CachePut(value = "user", key = "#user.id"),
         @CachePut(value = "user", key = "#user.username"),
         @CachePut(value = "user", key = "#user.email")}
    )
    public Information save(User user) {

        Objects.requireNonNull(user);
        informationRepository.save(Objects.requireNonNull(user.getInformation()));

        return user.getInformation();

    }

}
