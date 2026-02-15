package com.ecommerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.domain.UserRole;
import com.ecommerce.response.AuthResponse;
import com.ecommerce.response.SignUpRequest;
import com.ecommerce.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {
     private final AuthService authservice;

	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignUpRequest req){
		 String jwt = authservice.createUser(req);
		 AuthResponse res =  new AuthResponse();
		 res.setJwt(jwt);
		 res.setMessage("register success");
		 res.setRole(UserRole.ROLE_CUSTOMER);
		
		return ResponseEntity.ok(res);
	}
	@GetMapping("/ping")
	public String ping() {
		return "pong";
	}
}
