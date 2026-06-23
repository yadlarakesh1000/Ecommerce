package com.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce.models.VerificationCode;
import java.util.List;


public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long>{

	 VerificationCode findByEmail(String email);
	 VerificationCode  findByOtp(String otp);
	 @Transactional
	 void deleteByEmail(String email);
}
