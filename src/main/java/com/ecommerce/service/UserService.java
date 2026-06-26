package com.ecommerce.service;

import com.ecommerce.Exception.UserException;
import com.ecommerce.models.User;

public interface UserService {

	public User findUserByEmail(String email) throws UserException;
	public User findUserByJwt(String jwt) throws UserException;
}
