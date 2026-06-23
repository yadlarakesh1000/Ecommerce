package com.ecommerce.request;

import org.hibernate.validator.constraints.UniqueElements;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SignUpRequest {

	@Email
	@UniqueElements
	private String email;
	@NotNull
	private String fullname;
	private String otp;
}
