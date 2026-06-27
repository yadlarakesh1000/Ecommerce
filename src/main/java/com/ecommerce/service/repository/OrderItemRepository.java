package com.ecommerce.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.models.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
