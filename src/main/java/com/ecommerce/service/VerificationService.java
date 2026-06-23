package com.ecommerce.service;

import com.ecommerce.models.VerificationCode;

public interface VerificationService {
	VerificationCode createVerificationCode(String otp,String email);

}
