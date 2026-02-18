package com.ecommerce.service;

import com.ecommerce.models.User;

public interface UserService {

	public User findUserByEmail(String email) throws Exception;
}
