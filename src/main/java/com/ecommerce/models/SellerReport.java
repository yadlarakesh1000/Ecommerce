package com.ecommerce.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SellerReport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne
	private Seller seller;
	
	private Long totalEarnings;
	
	private Long totalSales;
	
	private Long totalRefunds;
	
	private Long totalTax;
	
	private Long netEarnings;
	
	private Integer cancelledOrders;
	
	private Integer totalTransactions;
	
	
	
}
