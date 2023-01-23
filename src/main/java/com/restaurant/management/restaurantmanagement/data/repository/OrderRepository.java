package com.restaurant.management.restaurantmanagement.data.repository;

import com.restaurant.management.restaurantmanagement.data.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long>
{
    List<Orders> findByUserId(final long userId);
}
