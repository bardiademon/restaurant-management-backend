package com.restaurant.management.restaurantmanagement.service;

import com.restaurant.management.restaurantmanagement.data.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public record UsersService(UsersRepository repository)
{
    @Autowired
    public UsersService
    {
    }
}
