package com.restaurant.management.restaurantmanagement.service;

import com.restaurant.management.restaurantmanagement.data.repository.FoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public record FoodsService(FoodsRepository repository)
{
    @Autowired
    public FoodsService
    {
    }
}
