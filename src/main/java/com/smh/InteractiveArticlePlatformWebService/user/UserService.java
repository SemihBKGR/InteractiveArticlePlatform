package com.smh.InteractiveArticlePlatformWebService.user;

public interface UserService {

    User findById(int id);
    User findByUsername(String username);
    User findByEmail(String email);
    User save(User user);
    void deleteById(int id);
}
