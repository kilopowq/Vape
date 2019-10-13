package com.kilopo.vape.api.auth;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.NonceExpiredException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class ExpiryAwareEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        if (authException instanceof NonceExpiredException)
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token Expired");
        else
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
    }
}
