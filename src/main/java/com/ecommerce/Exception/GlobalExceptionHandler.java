package com.ecommerce.Exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;




public class GlobalExceptionHandler {

	
	@ExceptionHandler(ProductException.class)
	public ResponseEntity<ErrorDetails> ProductExceptionHandler(ProductException ue, WebRequest req){
		
		ErrorDetails err= new ErrorDetails(ue.getMessage(),req.getDescription(false),LocalDateTime.now());
		
		return new ResponseEntity<ErrorDetails>(err,HttpStatus.BAD_REQUEST);
		
	}
	@ExceptionHandler(OrderException.class)
	public ResponseEntity<ErrorDetails>OrderExceptionHandler(OrderException ue, WebRequest req){
		
		ErrorDetails err= new ErrorDetails(ue.getMessage(),req.getDescription(false),LocalDateTime.now());
		
		return new ResponseEntity<ErrorDetails>(err,HttpStatus.BAD_REQUEST);
		
	}
	@ExceptionHandler(UserException.class)
	public ResponseEntity<ErrorDetails>UserExceptionHandler(UserException ue, WebRequest req){
		
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
	@ExceptionHandler(CartItemException.class)
	public ResponseEntity<ErrorDetails> handleCartItemException(CartItemException ex, WebRequest req) {
		ErrorDetails err= new ErrorDetails(ex.getMessage(),
				req.getDescription(false),
				LocalDateTime.now());

		return new ResponseEntity<ErrorDetails>(err,HttpStatus.BAD_REQUEST);
	}
	
}
