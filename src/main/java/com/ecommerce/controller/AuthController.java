package com.ecommerce.controller;

import org.springframework.boot.autoconfigure.graphql.GraphQlProperties.Http;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.domain.UserRole;
import com.ecommerce.models.VerificationCode;
import com.ecommerce.response.ApiResponse;
import com.ecommerce.response.AuthResponse;
import com.ecommerce.request.LoginRequest;
import com.ecommerce.request.SignUpRequest;
import com.ecommerce.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {
     private final AuthService authservice;

	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignUpRequest req) throws Exception{
		 String jwt = authservice.createUser(req);
		 AuthResponse res =  new AuthResponse();
		 res.setJwt(jwt);
		 res.setMessage("register success");
		 res.setRole(UserRole.ROLE_CUSTOMER);
		
		return ResponseEntity.ok(res);
	}
	
	@PostMapping("signin-otp")
	public ResponseEntity<ApiResponse> sentOtpHandler(@RequestBody VerificationCode req ) throws Exception {
         authservice.sentLoginOtp(req.getEmail());
         ApiResponse res = new ApiResponse();
         res.setMessage("OTP send Successfully");
       
		return ResponseEntity.ok(res);
	}
	@PostMapping("signin")
	public ResponseEntity<AuthResponse> signin(@RequestBody LoginRequest req) throws Exception{
		 AuthResponse authResponse= authservice.signin(req);
		return new ResponseEntity<>(authResponse,HttpStatus.OK);
	}
	
	

}
