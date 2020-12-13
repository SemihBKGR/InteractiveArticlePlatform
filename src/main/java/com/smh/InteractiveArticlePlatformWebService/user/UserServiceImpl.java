package com.smh.InteractiveArticlePlatformWebService.user;

import io.netty.util.internal.UnstableApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Cacheable("user")
    @Nullable
    @Override
    public User findById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    @Cacheable("user")
    @Nullable
    @Override
    public User findByUsername(String username) {
        Objects.requireNonNull(username);
        return userRepository.findByUsername(username);
    }

    @Cacheable("user")
    @Nullable
    @Override
    public User findByEmail(String email) {
        Objects.requireNonNull(email);
        return userRepository.findByEmail(email);
    }


    @Caching(evict =
                {@CacheEvict(value = "user", key = "#user.id"),
                 @CacheEvict(value = "user", key = "#user.username"),
                 @CacheEvict(value = "user", key = "#user.email")},
             put =
                {@CachePut(value = "user", key = "#user.id"),
                 @CachePut(value = "user", key = "#user.username"),
                 @CachePut(value = "user", key = "#user.email")}
             )
    @Override
    public User save(User user) {
        Objects.requireNonNull(user);
        return userRepository.save(user);
    }

    @Caching(evict =
            {@CacheEvict(value = "user", key = "#user.id"),
             @CacheEvict(value = "user", key = "#user.username"),
             @CacheEvict(value = "user", key = "#user.email")})
    @Override
    public void delete(User user) {
        userRepository.delete(user);
    }

}
