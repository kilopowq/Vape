package com.kilopo.vape.configuration;

import com.kilopo.vape.api.auth.AuthorizationHeaderFilter;
import com.kilopo.vape.api.auth.ExpiryAwareEntryPoint;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {
    private final ExpiryAwareEntryPoint expiryAwareEntryPoint;
    private final AuthorizationHeaderFilter authorizationHeaderFilter;

    SecurityConfigurer(ExpiryAwareEntryPoint expiryAwareEntryPoint, AuthorizationHeaderFilter authorizationHeaderFilter) {
        this.expiryAwareEntryPoint = expiryAwareEntryPoint;
        this.authorizationHeaderFilter = authorizationHeaderFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(expiryAwareEntryPoint)
                .and()
                .authorizeRequests()
                .antMatchers("/private/**").authenticated()
                .and()
                .addFilterBefore(authorizationHeaderFilter, BasicAuthenticationFilter.class);
    }

}
