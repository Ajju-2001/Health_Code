package com.healthinsurance.Health.PersonalDetailsController;

import java.util.ArrayList;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthinsurance.Health.PersonalDetailsService.UserService;
import com.healthinsurance.Health.PersonalEntities.Users;
import com.healthinsurance.Health.Response.ResponseHandler;

@RestController
@RequestMapping("/users")
public class UserController {
	private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseHandler register(@RequestBody Users user) { 
    	ResponseHandler response = new ResponseHandler();
    	try {
    		String result = userService.registerUser(user);
    		 response.setStatus(true); 
    		 response.setData(result); 
		} catch (Exception e) {
			response.setStatus(false); 
   		    response.setData(new ArrayList<>()); 
   		    response.setMessage(e.getMessage()); 
		}
        return response; 
    }

    @PostMapping("/login")
    public ResponseHandler login(@RequestBody Users user) { 
    	ResponseHandler response = new ResponseHandler();
    	try {
    		String result = userService.loginUser(user);
    		 response.setStatus(true); 
    		 response.setData(result); 
    		 response.setMessage("login Succefully!!");
		} catch (Exception e) {
			response.setStatus(false); 
   		    response.setData(new ArrayList<>()); 
   		    response.setMessage(e.getMessage()); 
		}
        return response; 
    }
}
