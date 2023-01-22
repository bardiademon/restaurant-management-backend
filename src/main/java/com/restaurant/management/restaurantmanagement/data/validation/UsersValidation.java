package com.restaurant.management.restaurantmanagement.data.validation;

import com.restaurant.management.restaurantmanagement.RestaurantManagementApplication;
import com.restaurant.management.restaurantmanagement.data.dto.LoginDto;
import com.restaurant.management.restaurantmanagement.data.dto.RegisterDto;
import com.restaurant.management.restaurantmanagement.data.entity.Users;
import com.restaurant.management.restaurantmanagement.data.enums.Roles;
import com.restaurant.management.restaurantmanagement.service.UsersService;

public class UsersValidation
{
    public static boolean loginValidation(final LoginDto loginDto)
    {
        return (loginDto != null && loginDto.getUsername() != null && !loginDto.getUsername().isEmpty() && loginDto.getPassword() != null && !loginDto.getPassword().isEmpty());
    }

    public static boolean registerValidation(final RegisterDto registerDto)
    {
        final boolean valid = registerDto != null &&
                registerDto.username() != null && !registerDto.username().isEmpty() &&
                registerDto.password() != null && !registerDto.password().isEmpty() &&
                registerDto.address() != null && !registerDto.address().isEmpty() &&
                registerDto.roleStr() != null && !registerDto.roleStr().isEmpty() &&

                registerDto.username().length() <= 50 && registerDto.password().length() <= 100 &&
                registerDto.name().length() <= 50 && registerDto.phone().length() <= 20;

        if (valid)
        {
            try
            {
                Roles.valueOf(registerDto.roleStr());
            }
            catch (Exception e)
            {
                return false;
            }
        }

        return valid;
    }

    public static boolean searchValidation(final String username)
    {
        return (username != null && !username.isEmpty());
    }

    public static Users tokenValidation(final String token , final UsersService usersService)
    {
        if (token != null && !token.isEmpty())
        {
            final Long userId = RestaurantManagementApplication.getJwt().getId(token);
            if (userId != null) return usersService.findUser(userId);
        }

        return null;
    }
}
