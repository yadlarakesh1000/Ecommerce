package com.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.models.VerificationCode;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long>{

	 VerificationCode findByEmail(String email);
}
