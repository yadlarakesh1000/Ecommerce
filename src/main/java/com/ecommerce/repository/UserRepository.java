package com.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.models.User;

public interface UserRepository extends JpaRepository<User, Long>{

	User findByEmail(String email);

	     
}
