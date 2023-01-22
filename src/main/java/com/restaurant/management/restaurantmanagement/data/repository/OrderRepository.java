package com.restaurant.management.restaurantmanagement.data.repository;

import com.restaurant.management.restaurantmanagement.data.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>
{
}
