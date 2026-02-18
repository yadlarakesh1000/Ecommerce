package com.ecommerce.service;

import com.ecommerce.response.SignUpRequest;

public interface AuthService {
     
	void sentLoginOtp(String email) throws Exception;
	String createUser(SignUpRequest req) throws Exception;
}
