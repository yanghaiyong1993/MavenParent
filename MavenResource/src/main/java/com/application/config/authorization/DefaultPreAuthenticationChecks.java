package com.application.config.authorization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsChecker;
import org.springframework.stereotype.Component;

/**
 * MavenParent
 *
 * @author yanghaiyong
 * 2020/8/29   11:39
 */
@Slf4j
@Component
public class DefaultPreAuthenticationChecks implements UserDetailsChecker {
    protected MessageSourceAccessor messages = SpringSecurityMessageSource.getAccessor();

    private DefaultPreAuthenticationChecks() {
    }

    public void check(UserDetails user) {
        if (!user.isAccountNonLocked()) {
            log.debug("User account is locked");
            throw new LockedException(this.messages.getMessage("locked", "User account is locked"));
        } else if (!user.isEnabled()) {
            log.debug("User account is disabled");
            throw new DisabledException(this.messages.getMessage("disabled", "User is disabled"));
        } else if (!user.isAccountNonExpired()) {
            log.debug("User account is expired");
            throw new AccountExpiredException(this.messages.getMessage("expired", "User account has expired"));
        }
    }
}
