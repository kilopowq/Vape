package com.kilopo.vape.api.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;

class RawHeaderAuthToken extends AbstractAuthenticationToken {
    private final String credentials;

    RawHeaderAuthToken(String credentials) {
        super(null);
        this.credentials = credentials;
    }

    @Override
    public String getCredentials() {
        return credentials;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}

