package com.kilopo.vape.api.auth;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

public interface UsernameProvider {
    String getUsername();
}

@Component
@Scope(scopeName = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
class UsernameProviderImpl implements UsernameProvider {
    private final AbstractAuthenticationToken token;

    UsernameProviderImpl() {
        this.token = (AbstractAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public String getUsername() {
        return token.getName();
    }
}