package com.ecommerce.Exception;

public class CategoryNotFoundException extends RuntimeException{
	public CategoryNotFoundException(String categoryNotFound){
		super(categoryNotFound);
	}

}
