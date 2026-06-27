package com.ecommerce.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ecommerce.Exception.OrderException;
import com.ecommerce.Exception.SellerException;
import com.ecommerce.domain.OrderStatus;
import com.ecommerce.models.Orders;
import com.ecommerce.models.Seller;
import com.ecommerce.response.ApiResponse;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.SellerService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller/orders")
public class SellerOrderController {

    private final OrderService orderService;

    private final SellerService sellerService;

  

    @GetMapping()
    public ResponseEntity<List<Orders>> getAllOrdersHandler(
            @RequestHeader("Authorization") String jwt
    ) throws SellerException {
        Seller seller=sellerService.getSellerProfile(jwt);
        List<Orders> orders=orderService.getShopsOrders(seller.getId());

        return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
    }

    @PatchMapping("/{orderId}/status/{orderStatus}")
    public ResponseEntity<Orders> updateOrderHandler(
            @RequestHeader("Authorization") String jwt,
            @PathVariable Long orderId,
            @PathVariable OrderStatus orderStatus
    ) throws OrderException {

        Orders orders=orderService.updateOrderStatus(orderId,orderStatus);

        return new ResponseEntity<>(orders,HttpStatus.ACCEPTED);
    }


    @DeleteMapping("/{orderId}/delete")
    public ResponseEntity<ApiResponse> deleteOrderHandler(@PathVariable Long orderId,
                                                          @RequestHeader("Authorization") String jwt) throws OrderException{
        orderService.deleteOrder(orderId);
        ApiResponse res=new ApiResponse("Order Deleted Successfully");
        System.out.println("delete method working....");
        return new ResponseEntity<>(res,HttpStatus.ACCEPTED);
    }

}
