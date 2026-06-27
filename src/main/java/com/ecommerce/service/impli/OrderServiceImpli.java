package com.ecommerce.service.impli;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ecommerce.Exception.OrderException;
import com.ecommerce.domain.OrderStatus;
import com.ecommerce.domain.PaymentStatus;
import com.ecommerce.models.Address;
import com.ecommerce.models.Cart;
import com.ecommerce.models.CartItem;
import com.ecommerce.models.OrderItem;
import com.ecommerce.models.Orders;
import com.ecommerce.models.User;
import com.ecommerce.repository.AddressRepository;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.CartService;
import com.ecommerce.service.OrderItemService;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.repository.OrderItemRepository;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class OrderServiceImpli implements OrderService {
	
	private final OrderRepository orderRepository;
	private final AddressRepository addressRepository;

	private final OrderItemRepository orderItemRepository;
	


	@Override
	public Set<Orders> createOrder(User user, Address shippAddress, Cart cart) {
		
//		shippAddress.setUser(user);
		if(!user.getAddresses().contains(shippAddress)){
			user.getAddresses().add(shippAddress);
		}


		Address address= addressRepository.save(shippAddress);



		Map<Long, List<CartItem>> itemsBySeller = cart.getCartItems().stream()
				.collect(Collectors.groupingBy(item -> item.getProduct().getSeller().getId()));

		Set<Orders> orders=new HashSet<>();

		for(Map.Entry<Long, List<CartItem>> entry:itemsBySeller.entrySet()){
			Long sellerId=entry.getKey();
			List<CartItem> cartItems=entry.getValue();

			int totalOrderPrice = cartItems.stream()
					.mapToInt(CartItem::getSellingPrice).sum();
			int totalItem=cartItems.stream().mapToInt(CartItem::getQuantity).sum();

			Orders createdOrder=new Orders();
			createdOrder.setUser(user);
			createdOrder.setSellerId(sellerId);
			createdOrder.setTotalMrpPrice(totalOrderPrice);
			createdOrder.setTotalSellingPrice(totalOrderPrice);
			createdOrder.setTotalItem(totalItem);
			createdOrder.setShippingAddress(address);
			createdOrder.setOrderStatus(OrderStatus.PENDING);
			createdOrder.getPaymentDetails().setStatus(PaymentStatus.PENDING);

			Orders savedOrder=orderRepository.save(createdOrder);
			orders.add(savedOrder);


			List<OrderItem> orderItems=new ArrayList<>();

			for(CartItem item: cartItems) {
				OrderItem orderItem=new OrderItem();

				orderItem.setOrders(savedOrder);
				orderItem.setMrpPrice(item.getMrpPrice());
				orderItem.setProduct(item.getProduct());
				orderItem.setQuantity(item.getQuantity());
				orderItem.setSize(item.getSize());
				orderItem.setUserId(item.getUserId());
				orderItem.setSellingPrice(item.getSellingPrice());

				savedOrder.getOrderItems().add(orderItem);

				OrderItem createdOrderItem=orderItemRepository.save(orderItem);

				orderItems.add(createdOrderItem);
			}

		}
		
		return orders;
		
	}

	@Override
	public Orders findOrderById(Long orderId) throws OrderException {
		Optional<Orders> opt=orderRepository.findById(orderId);
		
		if(opt.isPresent()) {
			return opt.get();
		}
		throw new OrderException("order not exist with id "+orderId);
	}

	@Override
	public List<Orders> usersOrderHistory(Long userId) {

		return orderRepository.findByUserId(userId);
	}

	@Override
	public List<Orders> getShopsOrders(Long sellerId) {

		return orderRepository.findBySellerIdOrderByOrderDateDesc(sellerId);
	}

	@Override
	public Orders updateOrderStatus(Long orderId, OrderStatus orderStatus)
			throws OrderException {
		Orders order=findOrderById(orderId);
		order.setOrderStatus(orderStatus);
		return orderRepository.save(order);
	}


	@Override
	public void deleteOrder(Long orderId) throws OrderException {
		Orders order = findOrderById(orderId);
		
		orderRepository.deleteById(orderId);
		
	}

	@Override
	public Orders cancelOrder(Long orderId, User user) throws OrderException {
		Orders order=this.findOrderById(orderId);
		if(user.getId()!=order.getUser().getId()){
			throw new OrderException("you can't perform this action "+orderId);
		}
		order.setOrderStatus(OrderStatus.CANCELLED);

		return orderRepository.save(order);
	}

}
