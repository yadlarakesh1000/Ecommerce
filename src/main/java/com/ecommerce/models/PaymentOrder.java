package com.ecommerce.models;

import java.util.HashSet;
import java.util.Set;

import com.ecommerce.domain.PaymentMethod;
import com.ecommerce.domain.PaymentOrderStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	private Long amount;
	
	private PaymentOrderStatus paymentStatus=PaymentOrderStatus.PENDING;
	
	private PaymentMethod paymentMethod;
	
	private String paymentLinkId;
	
	@ManyToOne
	private User user;
	
	@OneToMany
	private Set<Orders> orders=new HashSet<>();//why do we used set and orders(plural)
	
	
}
