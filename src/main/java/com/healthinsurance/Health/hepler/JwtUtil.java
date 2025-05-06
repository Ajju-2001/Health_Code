package com.healthinsurance.Health.hepler;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.healthinsurance.Health.PersonalEntities.Users;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	
	private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256); 
	
	private final long expirationTimeMs = 1000 * 60 * 60; 

    public String generateToken(Users user) {
        
    	Map<String, Object> claims = new HashMap<>();
    	claims.put("userId", user.getUserId());
    	claims.put("password", user.getPassword()); 
        

        return Jwts.builder()
        		.setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeMs))
                .signWith(SECRET_KEY) 
                .compact();
    }
    
 
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    

    public boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
}
