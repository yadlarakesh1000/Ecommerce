package com.ecommerce.controller;

public class CategoryNotFoundException extends RuntimeException{
	CategoryNotFoundException(String categoryNotFound){
		super(categoryNotFound);
	}

}
