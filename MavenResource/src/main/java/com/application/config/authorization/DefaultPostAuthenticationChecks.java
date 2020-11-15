package com.application.config.authorization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.stereotype.Component;

/**
 * MavenParent
 *
 * @author yanghaiyong
 * 2020/8/29   11:37
 */
@Slf4j
@Component
public class DefaultPostAuthenticationChecks implements UserDetailsChecker {
    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    private DefaultPostAuthenticationChecks() {
    }

    public void check(UserDetails user) {
        if (!user.isCredentialsNonExpired()) {
            log.debug("User account credentials have expired");
            throw new CredentialsExpiredException(this.messages.getMessage("AbstractUserDetailsAuthenticationProvider.credentialsExpired", "User credentials have expired"));
        }
    }
}
