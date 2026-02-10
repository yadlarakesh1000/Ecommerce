package com.ecommerce.service;

import com.ecommerce.response.SignUpRequest;

public interface AuthService {
         
	String createUser(SignUpRequest req);
}
