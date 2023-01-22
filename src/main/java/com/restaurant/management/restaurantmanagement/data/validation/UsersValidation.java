package com.restaurant.management.restaurantmanagement.data.validation;

import com.restaurant.management.restaurantmanagement.data.dto.LoginDto;
import com.restaurant.management.restaurantmanagement.data.dto.RegisterDto;

public class UsersValidation
{
    public static boolean loginValidation(final LoginDto loginDto)
    {
        return (loginDto != null && loginDto.getUsername() != null && !loginDto.getUsername().isEmpty() && loginDto.getPassword() != null && !loginDto.getPassword().isEmpty());
    }

    public static boolean registerValidation(final RegisterDto registerDto)
    {
        return (registerDto != null &&
                registerDto.username() != null && !registerDto.username().isEmpty() &&
                registerDto.password() != null && !registerDto.password().isEmpty() &&
                registerDto.address() != null && !registerDto.address().isEmpty() &&

                registerDto.username().length() <= 50 && registerDto.password().length() <= 100 &&
                registerDto.name().length() <= 50 && registerDto.phone().length() <= 20
        );
    }
}
