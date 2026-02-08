package com.ecommerce.models;

import java.util.List;

import lombok.Data;

@Data
public class Home {

	private List<Deal> deals;
	
	private List<HomeCategory> grid;
	
	private List<HomeCategory> shopByCategories;
	
	private List<HomeCategory> electricCategories;
	
	private List<HomeCategory> dealCategories;
	
}
