package com.ecommerce.response;

import lombok.Data;

@Data
public class SignUpRequest {

	   private String email;
	   private String fullname;
	   private String otp;
}
