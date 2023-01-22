package com.restaurant.management.restaurantmanagement.service;

import com.restaurant.management.restaurantmanagement.data.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public record OrderService(OrderRepository repository)
{
    @Autowired
    public OrderService
    {
    }
}
