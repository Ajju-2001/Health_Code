package com.healthinsurance.Health.hepler;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

import javax.naming.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.healthinsurance.Health.PersonalDetailsRepository.UserRepository;
import com.healthinsurance.Health.PersonalEntities.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class TokenGenerator {

	@Autowired
	private  UserRepository userRepository;
	
	@Autowired
	private Auth Auth;
	
	    // Secret key for signing the token
	    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // HMAC-SHA256

	    
//	    public boolean authenticateUser(String username, String password) {
//	        // Query the database to find the user by username
//	        Optional<User> user = userRepository.findByUsername(username);
//
//	        // Check if user exists and password is correct (assuming you hash passwords)
//	        if (user.isPresent() && user.get().getPassword().equals(password)) {
//	            
//		        System.out.println("Generated JWT Token:");
//
//	        	return true;  // Valid credentials
//
//	        }
//	        return false;  // Invalid credentials
//	    }

	    public static String generateToken(String username) {
	        long nowMillis = System.currentTimeMillis();
	        long expMillis = nowMillis + 3600000; // Token valid for 1 hour
	        Date now = new Date(nowMillis);
	        Date expiry = new Date(expMillis);

	        return Jwts.builder()
	                .setSubject(username)
	                .setIssuedAt(now)
	                .setExpiration(expiry)
	                .signWith(key)
	                .compact();
	    }
//        System.out.println("Generated JWT Token:");

	    public String authenticateAndGenerateToken(String username, String password) throws AuthenticationException {
	        // Validate username and password
	        if (Auth.authenticateUser(username, password)) {
	            // Generate and return the token if credentials are valid
	            return generateToken(username);
	        } else {
	            throw new AuthenticationException("Invalid username or password");
	        }
	    }

//	    public static void main(String[] args) {
//	        String token = generateToken("myUser123");
//	        System.out.println("Generated JWT Token:");
//	        System.out.println(token);
//	    }
	}

