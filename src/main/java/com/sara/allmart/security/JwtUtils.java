package com.sara.allmart.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {
    @Value("${allmart.app.jwtSecret}")
    private String jwtSecret;

    @Value("${allmart.app.jwtExpirationMs}")
    private int jwtExpirationMs;
}
