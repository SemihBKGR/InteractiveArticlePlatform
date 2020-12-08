package com.smh.InteractiveArticlePlatformWebService.user;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User,Integer>{

    User findByUsername(String username);
    User findByEmail(String email);

}
