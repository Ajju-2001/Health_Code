package com.healthinsurance.Health.hepler;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.healthinsurance.Health.PersonalEntities.Users;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {
	
//	private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256); 
	
	@Value("${jwt.secret}")
    private String secret;

    private Key SECRET_KEY;
	
	private final long expirationTimeMs = 1000 * 60 * 60 * 24; 
	
	@PostConstruct
    public void init() {
        this.SECRET_KEY = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(Users user) {
        
    	Map<String, Object> claims = new HashMap<>();
    	claims.put("userId", user.getUserId()); 
    	claims.put("email", user.getEmail());
        claims.put("role", user.getRole());
         

        return Jwts.builder()
        		.setClaims(claims) 
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeMs))
                .signWith(SECRET_KEY) 
                .compact();
    }
    
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
 
    public String extractUsername(String token) {
    	return extractAllClaims(token).getSubject(); 
    }
    
    public Integer extractUserId(String token) {
    	 return (Integer) extractAllClaims(token).get("userId");
    } 
    
    public String extractEmail(String token) {
    	return (String) extractAllClaims(token).get("email");
    }

    public String extractRole(String token) {
    	return (String) extractAllClaims(token).get("role");
    }

   

    public boolean validateToken(String token,Integer userId, String username, String email, String role) {
    	Integer extactInteger = extractUserId(token);
    	String extractedUsername = extractUsername(token);
        String extractedEmail = extractEmail(token);  
        String extractedRole = extractRole(token);  
        return (
        	userId.equals(extactInteger) &&
            username.equals(extractedUsername) &&
            email.equals(extractedEmail) &&
            role.equals(extractedRole) &&
            !isTokenExpired(token) 
        );
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
