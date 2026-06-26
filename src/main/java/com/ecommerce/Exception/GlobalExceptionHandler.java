package com.ecommerce.Exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.ecommerce.controller.CategoryNotFoundException;


public class GlobalExceptionHandler {

	
	@ExceptionHandler(ProductException.class)
	public ResponseEntity<ErrorDetails> ProductExceptionHandler(ProductException ue, WebRequest req){
		
		ErrorDetails err= new ErrorDetails(ue.getMessage(),req.getDescription(false),LocalDateTime.now());
		
		return new ResponseEntity<ErrorDetails>(err,HttpStatus.BAD_REQUEST);
		
	}
	
	
	@ExceptionHandler(SellerException.class)
	public ResponseEntity<ErrorDetails> handleSellerException(SellerException ex, WebRequest req) {
		ErrorDetails err= new ErrorDetails(ex.getMessage(),
				req.getDescription(false),
				LocalDateTime.now());

		return new ResponseEntity<ErrorDetails>(err,HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(CategoryNotFoundException.class)
	public ResponseEntity<ErrorDetails> handleCategoryNotFoundException(CategoryNotFoundException ex, WebRequest req) {
		ErrorDetails err= new ErrorDetails(ex.getMessage(),
				req.getDescription(false),
				LocalDateTime.now());

		return new ResponseEntity<ErrorDetails>(err,HttpStatus.BAD_REQUEST);
	}

	
}
