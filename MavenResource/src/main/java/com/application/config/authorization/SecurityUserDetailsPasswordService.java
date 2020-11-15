package com.application.config.authorization;

import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class SecurityUserDetailsPasswordService implements UserDetailsPasswordService {

    @Resource
    private ApplicationContext applicationContext;

    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        return user;
    }
}
