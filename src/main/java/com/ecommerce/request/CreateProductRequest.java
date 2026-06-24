package com.ecommerce.request;

import java.util.List;
import java.util.Set;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class CreateProductRequest {
      
	 private String title;

	    @Column(length = 2000)
	    private String description;

	    private int mrpPrice;

	    private int sellingPrice;

	    private String brand;

	    private String color;

	    @Column(length = 5000)
	    private List<String> images;

	    private String category;
	    private String category2;
	    private String category3;

	    private Set<String>sizes;
	    
}
