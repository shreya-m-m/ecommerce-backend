package com.ecommerce.backend.config;


import java.util.Base64;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtProvider {
    
    
	  SecretKey key = Keys.hmacShaKeyFor(JwtTokenProvider.SECRET_KEY.getBytes());
    
	  public String generateToken(Authentication auth) {
	    // Set the issued at date to now
	    Date now = new Date();
	    // Set expiration to 2 hours from now
	    Date expirationDate = new Date(now.getTime() + 24 * 60 * 60 * 1000); 

	    String jwt = Jwts.builder()
	            .setIssuedAt(now)
	            .setExpiration(expirationDate) 
	            .claim("email", auth.getName()) 
	            .signWith(key, SignatureAlgorithm.HS256) 
	            .compact();

	    return jwt;
    }
    
    public String getEmailFromToken(String jwtToken) {
        jwtToken = jwtToken.substring(7); 
        
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
        
        String email = String.valueOf(claims.get("email"));
        
        return email;
    }
}
