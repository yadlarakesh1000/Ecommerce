package com.ecommerce.service;

import com.ecommerce.Exception.CartItemException;
import com.ecommerce.Exception.UserException;
import com.ecommerce.models.CartItem;


public interface CartItemService {
	
	
public CartItem updateCartItem(Long userId, Long id,CartItem cartItem) throws CartItemException, UserException;
	
	public void removeCartItem(Long userId,Long cartItemId) throws CartItemException, UserException;
	
	public CartItem findCartItemById(Long cartItemId) throws CartItemException;
	

}
