package com.application.config.authorization;

import org.springframework.context.ApplicationContext;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collections;

@Component
public class SecurityUserDetailsService implements UserDetailsService {

    @Resource
    private ApplicationContext applicationContext;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        PasswordEncoder passwordEncoder = applicationContext.getBean(PasswordEncoder.class);
        return new User("admin", passwordEncoder.encode("admin"), Collections.emptyList());
    }
}
