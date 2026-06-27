package com.ecommerce.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.ecommerce.models.Orders;

public interface OrderRepository extends JpaRepository<Orders,Long> {

    List<Orders>findByUserId(Long userId);
    List<Orders> findBySellerIdOrderByOrderDateDesc(Long sellerId);
    List<Orders> findBySellerIdAndOrderDateBetween(Long sellerId,LocalDateTime startDate, LocalDateTime endDate);

}
