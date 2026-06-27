package com.ecommerce.service;

import com.ecommerce.models.OrderItem;

public interface OrderItemService {
	OrderItem getOrderItemById(Long id) throws Exception;
}
