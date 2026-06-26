package com.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.models.Cart;
import com.ecommerce.models.CartItem;
import com.ecommerce.models.Product;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	CartItem findByCartAndProductAndSize(Cart cart, Product product, String size);

}
