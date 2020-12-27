package com.smh.InteractiveArticlePlatformWebService.user;

import java.util.List;

public interface UserService {

    User findById(int id);
    User findByUsername(String username);
    User findByEmail(String email);
    User save(User user);
    void delete(User user);
    List<User> searchUser(String searchText);
    List<User> searchByUsername(String username);
    List<User> searchByEmail(String email);

}
