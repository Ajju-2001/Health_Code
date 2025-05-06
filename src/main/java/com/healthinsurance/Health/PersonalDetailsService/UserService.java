package com.healthinsurance.Health.PersonalDetailsService;

import java.util.Optional;

import org.aspectj.apache.bcel.classfile.Module.Uses;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.healthinsurance.Health.PersonalDetailsRepository.UserRepository;
import com.healthinsurance.Health.PersonalEntities.Users;
import com.healthinsurance.Health.hepler.JwtUtil;

@Service
public class UserService {
	private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public String registerUser(Users user) {
    	
    	if (user.getUsername() == null || user.getUsername().trim().isEmpty() ||
    	        user.getPassword() == null || user.getPassword().trim().isEmpty()) {
    	        throw new IllegalArgumentException("Username and password must not be empty");
    	    }
    	
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
        	throw new IllegalArgumentException("Username already exists");
        }

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);  

        userRepository.save(user); 
        return "User registered successfully";
    } 

    public String loginUser(Users user) {
    	
    	if (user.getUsername() == null || user.getUsername().trim().isEmpty() ||
    	        user.getPassword() == null || user.getPassword().trim().isEmpty()) {
    	        throw new IllegalArgumentException("Username and password must not be empty");
    	    }
    	
        Optional<Users> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            Users foundUser = existingUser.get(); 

            if (passwordEncoder.matches(user.getPassword(), foundUser.getPassword())) {
                return jwtUtil.generateToken(foundUser);  
            }
        }
        throw new IllegalArgumentException("Invalid username or password");  
    }

} 
