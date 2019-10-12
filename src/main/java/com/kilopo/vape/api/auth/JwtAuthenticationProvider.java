package com.kilopo.vape.api.auth;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.www.NonceExpiredException;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationProvider.class);

    private final JwtAuthenticationToken.Factory tokenFactory;

    JwtAuthenticationProvider(JwtAuthenticationToken.Factory tokenFactory) {
        this.tokenFactory = tokenFactory;
    }

    @Override
    public JwtAuthenticationToken authenticate(Authentication authentication) throws AuthenticationException {
        final RawHeaderAuthToken rawToken = (RawHeaderAuthToken) authentication;

        try {
            return tokenFactory.create(rawToken.getCredentials());
        } catch (ExpiredJwtException e) {
            logger.debug("JWT exception occurred:", e);
            throw new NonceExpiredException(e.getMessage(), e);
        } catch (JwtException e) {
            logger.debug("JWT exception occurred:", e);
            throw new BadCredentialsException(e.getMessage(), e);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(RawHeaderAuthToken.class);
    }
}
