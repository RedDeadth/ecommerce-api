package com.ecommerce.domain.repository;

import com.ecommerce.domain.model.Order;
import com.ecommerce.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByCreatedAtDesc(User user);
}
