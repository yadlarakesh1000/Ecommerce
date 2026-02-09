package com.ecommerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.models.User;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.response.SignUpRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

	private final UserRepository userRepository;
	@PostMapping("/signup")
	public ResponseEntity<User> createUserHandler(@RequestBody SignUpRequest req){
		 User user = new User();
		 user.setEmail(req.getEmail());
		 user.setFullname(req.getFullname());
		 User savedUser = userRepository.save(user);
		 return ResponseEntity.ok(savedUser);
		 
	}
}
