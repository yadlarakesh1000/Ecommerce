package com.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.models.Seller;

public interface SellerRepository extends JpaRepository<Seller, Long> {

	Seller findByEmail(String email);
	
}
