package com.ecommerce.service;

import com.ecommerce.Exception.ProductException;
import com.ecommerce.models.Cart;
import com.ecommerce.models.CartItem;
import com.ecommerce.models.Product;
import com.ecommerce.models.User;

public interface CartService {
	
	public CartItem addCartItem(User user,
			                Product product,
			                String size,
			                int quantity) throws ProductException;
	public Cart findUserCart(User user);
	

}
