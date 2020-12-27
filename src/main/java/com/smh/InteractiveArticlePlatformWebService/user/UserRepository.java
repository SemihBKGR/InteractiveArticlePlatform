package com.smh.InteractiveArticlePlatformWebService.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends CrudRepository<User,Integer>{

    User findByUsername(String username);
    User findByEmail(String email);

    @Query(value = "SELECT * FROM users WHERE username LIKE %?1% OR email LIKE %?1%",nativeQuery = true)
    List<User> searchUser(String searchText);

    @Query(value = "SELECT * FROM users WHERE username LIKE %?1%",nativeQuery = true)
    List<User> searchUserByUsername(String username);

    @Query(value = "SELECT * FROM users WHERE email LIKE %?1%",nativeQuery = true)
    List<User> searchUserByEmail(String email);

}
