package com.kilopo.vape.api.auth;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class AuthorizationHeaderFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String rawHeader = request.getHeader("Authorization");

        parseHeader(rawHeader).ifPresent(token -> SecurityContextHolder.getContext().setAuthentication(new RawHeaderAuthToken(token)));

        filterChain.doFilter(request, response);
    }

    private Optional<String> parseHeader(final String rawHeader) {
        if(rawHeader == null || !rawHeader.startsWith("Bearer "))
            return Optional.empty();

        return Optional.of(rawHeader.substring(7));
    }


}
