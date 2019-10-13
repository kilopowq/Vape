package com.kilopo.vape.api.auth;

import com.google.common.collect.ImmutableList;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

import static com.google.common.collect.ImmutableList.toImmutableList;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private final String username;
    private final String jwtToken;

    private JwtAuthenticationToken(String username, String jwtToken, List<String> roles, Claims details) {
        super(rolesToAuthorities(roles));
        this.username = username;
        this.jwtToken = jwtToken;
        setDetails(details);
    }

    @Override
    public String getPrincipal() {
        return username;
    }

    @Override
    public String getCredentials() {
        return jwtToken;
    }

    @Override
    public Claims getDetails() {
        return (Claims) super.getDetails();
    }

    private static List<? extends GrantedAuthority> rolesToAuthorities(List<String> roles) {
        return roles.stream()
                .map(role -> {
                    Assert.isTrue(!role.startsWith("ROLE_"), role
                            + " cannot start with ROLE_ (it is automatically added)");
                    return new SimpleGrantedAuthority("ROLE_"+role);
                })
                .collect(toImmutableList());
    }

    @Component
    static class Factory {
        private final JwtParser jwtParser;

        Factory(JwtParser jwtParser) {
            this.jwtParser = jwtParser;
        }

        JwtAuthenticationToken create(String jwtToken) {
            final Claims claims = jwtParser.parseClaimsJws(jwtToken).getBody();

            @SuppressWarnings("unchecked")
            final List<String> roles = Optional.ofNullable((List<String>)claims.get("roles", List.class))
                    .orElse(ImmutableList.of())
                    .stream()
                    .map(String::toUpperCase)
                    .collect(toImmutableList());

            final String username = claims.get("preferred_username", String.class);

            return new JwtAuthenticationToken(username, jwtToken, roles, claims);
        }

    }
}

