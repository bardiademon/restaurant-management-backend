package com.restaurant.management.restaurantmanagement.data.validation;

import com.restaurant.management.restaurantmanagement.data.dto.LoginDto;

public class UsersValidation
{
    public static boolean loginValidation(final LoginDto loginDto)
    {
        return (loginDto != null && loginDto.getUsername() != null && !loginDto.getUsername().isEmpty() && loginDto.getPassword() != null && !loginDto.getPassword().isEmpty());
    }
}
