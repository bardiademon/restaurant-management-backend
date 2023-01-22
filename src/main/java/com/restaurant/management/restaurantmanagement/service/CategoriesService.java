package com.restaurant.management.restaurantmanagement.service;

import com.restaurant.management.restaurantmanagement.data.repository.CategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public record CategoriesService(CategoriesRepository repository)
{
    @Autowired
    public CategoriesService
    {
    }
}
