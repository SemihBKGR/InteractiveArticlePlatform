package com.smh.InteractiveArticlePlatformWebService.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Nullable
    @Override
    public User findById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    @Nullable
    @Override
    public User findByUsername(String username) {
        Objects.requireNonNull(username);
        return userRepository.findByUsername(username);
    }

    @Nullable
    @Override
    public User findByEmail(String email) {
        Objects.requireNonNull(email);
        return userRepository.findByEmail(email);
    }

    @Override
    public User save(User user) {
        Objects.requireNonNull(user);
        return userRepository.save(user);
    }

    @Override
    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

}
