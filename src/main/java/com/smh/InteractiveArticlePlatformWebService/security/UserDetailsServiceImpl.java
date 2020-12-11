package com.smh.InteractiveArticlePlatformWebService.security;

import com.smh.InteractiveArticlePlatformWebService.user.User;
import com.smh.InteractiveArticlePlatformWebService.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user=userService.findByUsername(s);
        if(user==null){
            throw new UsernameNotFoundException("No user with username '"+s+"'");
        }
        return warpUser(user);
    }

    // Wrap entity user to sub-class of UserDetails.
    private UserDetails warpUser(User user){
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),user.getPassword(), new ArrayList<>(0));
    }

}
