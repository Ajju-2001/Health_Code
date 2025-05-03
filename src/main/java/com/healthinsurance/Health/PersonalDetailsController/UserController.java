package com.healthinsurance.Health.PersonalDetailsController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.healthinsurance.Health.PersonalEntities.User;
import com.healthinsurance.Health.hepler.TokenGenerator;


@RequestMapping("/users")
@RestController
public class UserController {

	@Autowired
	private TokenGenerator  tokenGenerator;
	
//	@PostMapping("/login")
//	String testing(@RequestBody User user) {
//		String token = null;
//		try {
//			token = TokenGenerator.generateToken("myUser123");
//		} catch (Exception e) {
//			e.printStackTrace();
//			// TODO: handle exception
//		}
//		
//		return token;
//		
//	}
}

