package com.ecommerce.domain;

public enum AccountStatus {
        
	PENDING_VERIFICATION, //account created but not yet verified
	ACTIVE,           //account is in active
	SUSPENDED,           //account is temporarily suspended
	DEACTIVATED,        //user deactivated temporarily his account
	BANNED,          //account is permanently banned
	CLOSED          //account s permanently closed by user
	
	
	
	
}
