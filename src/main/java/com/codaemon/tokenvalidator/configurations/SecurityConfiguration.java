package com.codaemon.tokenvalidator.configurations;

import com.codaemon.tokenvalidator.filters.SecurityFilters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class SecurityConfiguration {

    @Value("${jwks-uri}")
    private String JWKS_URI;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(
                        authManagerReqMatcherRegistry ->
                                authManagerReqMatcherRegistry.anyRequest().authenticated())
                .addFilterAfter(new SecurityFilters(JWKS_URI), BasicAuthenticationFilter.class)
                .build();
    }
}
