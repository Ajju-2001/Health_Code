package com.healthinsurance.Health.hepler;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.healthinsurance.Health.PersonalDetailsRepository.UserRepository;
import com.healthinsurance.Health.PersonalEntities.User;

@Component
public class Auth {
	
	@Autowired
	private  UserRepository userRepository;
	
	public boolean authenticateUser(String username, String password) {
        // Query the database to find the user by username
        Optional<User> user = userRepository.findByUsername(username);

        // Check if user exists and password is correct (assuming you hash passwords)
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            
	        System.out.println("Generated JWT Token:");

        	return true;  // Valid credentials

        }
        return false;  // Invalid credentials
    }
}
