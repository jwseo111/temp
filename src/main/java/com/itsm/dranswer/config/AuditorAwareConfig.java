package com.itsm.dranswer.config;

/*
 * @package : com.itsm.dranswer.config
 * @name : AuditorAwareConfig.java
 * @date : 2021-05-24 오후 2:30
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class AuditorAwareConfig implements AuditorAware<Long> {

    public Optional<Long> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(authentication -> {

                    Collection<? extends GrantedAuthority> auth = authentication.getAuthorities();
                    boolean isUser = auth.contains(new SimpleGrantedAuthority("USER"));

                    if (isUser) {
                        return Long.valueOf(authentication.getName());
                    }

                    return null;

                });
    }
}
