package com.ecommerce.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.stripe.exception.StripeException;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;


import com.ecommerce.Exception.OrderException;
import com.ecommerce.Exception.SellerException;
import com.ecommerce.Exception.UserException;
import com.ecommerce.domain.PaymentMethod;
import com.ecommerce.models.Address;
import com.ecommerce.models.Cart;
import com.ecommerce.models.OrderItem;
import com.ecommerce.models.Orders;
import com.ecommerce.models.PaymentOrder;
import com.ecommerce.models.Seller;
import com.ecommerce.models.SellerReport;
import com.ecommerce.models.User;
import com.ecommerce.repository.PaymentOrderRepository;
import com.ecommerce.response.PaymentLinkResponse;
import com.ecommerce.service.CartService;
import com.ecommerce.service.OrderItemService;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.PaymentService;
import com.ecommerce.service.SellerReportService;
import com.ecommerce.service.SellerService;
import com.ecommerce.service.UserService;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
	
	private final OrderService orderService;
	private final UserService userService;
	private final OrderItemService orderItemService;
	private final CartService cartService;
	private final PaymentService paymentService;
	private final PaymentOrderRepository paymentOrderRepository;
	private final SellerReportService sellerReportService;
	private final SellerService sellerService;

	
	@PostMapping()
	public ResponseEntity<PaymentLinkResponse> createOrderHandler(
			@RequestBody Address spippingAddress,
			@RequestParam PaymentMethod paymentMethod,
			@RequestHeader("Authorization")String jwt)
            throws UserException, RazorpayException, StripeException {
		
		User user=userService.findUserByJwt(jwt);
		Cart cart=cartService.findUserCart(user);
		Set<Orders> orders =orderService.createOrder(user, spippingAddress,cart);

		PaymentOrder paymentOrder=paymentService.createOrder(user,orders);

		PaymentLinkResponse res = new PaymentLinkResponse();

		if(paymentMethod.equals(PaymentMethod.RAZORPAY)){
			PaymentLink payment=paymentService.createRazorpayPaymentLink(user,
					paymentOrder.getAmount(),
					paymentOrder.getId());
			String paymentUrl=payment.get("short_url");
			String paymentUrlId=payment.get("id");


			res.setPayment_LinkUrl(paymentUrl);
//			res.setPayment_link_id(paymentUrlId);
			paymentOrder.setPaymentLinkId(paymentUrlId);
			paymentOrderRepository.save(paymentOrder);
		}
		else{
			String paymentUrl=paymentService.createStripePaymentLink(user,
					paymentOrder.getAmount(),
					paymentOrder.getId());
			res.setPayment_LinkUrl(paymentUrl);
		}
		return new ResponseEntity<>(res,HttpStatus.OK);

	}
	
	@GetMapping("/user")
	public ResponseEntity< List<Orders>> usersOrderHistoryHandler(
			@RequestHeader("Authorization")
	String jwt) throws UserException{
		
		User user=userService.findUserByJwt(jwt);
		List<Orders> orders=orderService.usersOrderHistory(user.getId());
		return new ResponseEntity<>(orders,HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/{orderId}")
	public ResponseEntity< Orders> getOrderById(@PathVariable Long orderId, @RequestHeader("Authorization")
	String jwt) throws OrderException, UserException{
		
		User user = userService.findUserByJwt(jwt);
		Orders orders=orderService.findOrderById(orderId);
		return new ResponseEntity<>(orders,HttpStatus.ACCEPTED);
	}

	@GetMapping("/item/{orderItemId}")
	public ResponseEntity<OrderItem> getOrderItemById(
			@PathVariable Long orderItemId, @RequestHeader("Authorization")
	String jwt) throws Exception {
		System.out.println("------- controller ");
		User user = userService.findUserByJwt(jwt);
		OrderItem orderItem=orderItemService.getOrderItemById(orderItemId);
		return new ResponseEntity<>(orderItem,HttpStatus.ACCEPTED);
	}

	@PutMapping("/{orderId}/cancel")
	public ResponseEntity<Orders> cancelOrder(
			@PathVariable Long orderId,
			@RequestHeader("Authorization") String jwt
	) throws UserException, OrderException, SellerException {
		User user=userService.findUserByJwt(jwt);
		Orders order=orderService.cancelOrder(orderId,user);

		Seller seller= sellerService.getSellerById(order.getSellerId());
		SellerReport report=sellerReportService.getSellerReport(seller);

		report.setCancelledOrders(report.getCancelledOrders()+1);
		report.setTotalRefunds(report.getTotalRefunds()+order.getTotalSellingPrice());
		sellerReportService.updateSellerReport(report);

		return ResponseEntity.ok(order);
	}

}
