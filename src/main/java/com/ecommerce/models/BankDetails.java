package com.ecommerce.models;

import lombok.Data;

@Data
public class BankDetails {

	private String accountHolderName;
	private String bankName;
	private String accountNumber;
	private String ifscCode;
}
