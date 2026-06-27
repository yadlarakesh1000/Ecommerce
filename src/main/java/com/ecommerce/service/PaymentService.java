package com.ecommerce.service;

import java.util.Set;

import com.ecommerce.models.Orders;
import com.ecommerce.models.PaymentOrder;
import com.ecommerce.models.User;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;


public interface PaymentService {

    PaymentOrder createOrder(User user,
                             Set<Orders> orders);

    PaymentOrder getPaymentOrderById(Long id) throws Exception;

    PaymentOrder getPaymentOrderByPaymentId(String paymentId) throws Exception;

    Boolean ProceedPaymentOrder (PaymentOrder paymentOrder,
                                 String paymentId, String paymentLinkId) throws RazorpayException;

    PaymentLink createRazorpayPaymentLink(User user,
                                          Long Amount,
                                          Long orderId) throws RazorpayException;

    String createStripePaymentLink(User user, Long Amount,
                                            Long orderId) throws StripeException;
}

