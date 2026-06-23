package com.ecommerce.service;

import com.ecommerce.request.SignUpRequest;
import com.ecommerce.request.LoginRequest;
import com.ecommerce.response.AuthResponse;

public interface AuthService {
     
	void sentLoginOtp(String email) throws Exception;
	String createUser(SignUpRequest req) throws Exception;
	AuthResponse signin(LoginRequest req) throws Exception;
}
