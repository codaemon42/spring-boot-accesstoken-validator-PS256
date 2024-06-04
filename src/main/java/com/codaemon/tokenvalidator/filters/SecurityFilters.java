package com.codaemon.tokenvalidator.filters;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.*;
import com.nimbusds.jose.jwk.source.*;
import com.nimbusds.jose.proc.*;
import com.nimbusds.jwt.*;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.util.Collections;
import java.util.Date;


public class SecurityFilters implements Filter {

    Logger logger = LoggerFactory.getLogger(getClass());

    private final JWKSource<SecurityContext> jwkSource;

    public SecurityFilters(String jwksUri) throws MalformedURLException {
        URL jwkSetURL = new URL(jwksUri);
        this.jwkSource = new RemoteJWKSet<>(jwkSetURL);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
            String authHeader = httpRequest.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);  // Extract the token

                SignedJWT signedJWT = SignedJWT.parse(token);
                JWSVerifier verifier = new RSASSAVerifier((RSAPublicKey) jwkSource.get(new JWKSelector(
                        new JWKMatcher.Builder()
                                .keyID(signedJWT.getHeader().getKeyID())
                                .build()), null).get(0).toRSAKey().toPublicKey());

                if (signedJWT.verify(verifier)) {
                    if(new Date().before(signedJWT.getJWTClaimsSet().getExpirationTime())) {
                        SecurityContextHolder.getContext().setAuthentication(
                                new UsernamePasswordAuthenticationToken(signedJWT.getJWTClaimsSet().getSubject(), null,
                                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))));
                    } else {
                        throw new JOSEException("Token Expired !");
                    }
                } else {
                    throw new JOSEException("Signature verification failed");
                }
            }
        } catch (Exception e) {
            logger.error("Exception caught in JWT processing", e);
            HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
        SecurityContextHolder.clearContext();
    }
}
