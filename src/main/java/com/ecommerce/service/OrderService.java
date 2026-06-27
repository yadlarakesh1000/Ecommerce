package com.ecommerce.service;

import java.util.List;
import java.util.Set;

import com.ecommerce.Exception.OrderException;
import com.ecommerce.domain.OrderStatus;
import com.ecommerce.models.Address;
import com.ecommerce.models.Cart;
import com.ecommerce.models.Orders;
import com.ecommerce.models.User;


public interface OrderService {
	
public Set<Orders> createOrder(User user, Address shippingAddress, Cart cart);
	
	public Orders findOrderById(Long orderId) throws OrderException;
	
	public List<Orders> usersOrderHistory(Long userId);
	
	public List<Orders>getShopsOrders(Long sellerId);

	public Orders updateOrderStatus(Long orderId,
								   OrderStatus orderStatus)
			throws OrderException;
	
	public void deleteOrder(Long orderId) throws OrderException;

	Orders cancelOrder(Long orderId,User user) throws OrderException;
	
}