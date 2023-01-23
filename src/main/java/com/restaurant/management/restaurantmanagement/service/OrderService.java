package com.restaurant.management.restaurantmanagement.service;

import com.restaurant.management.restaurantmanagement.data.entity.Orders;
import com.restaurant.management.restaurantmanagement.data.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public record OrderService(OrderRepository repository)
{
    @Autowired
    public OrderService
    {
    }

    public List<Orders> find(final long userId)
    {
        return repository.findByUserId(userId);
    }
}
