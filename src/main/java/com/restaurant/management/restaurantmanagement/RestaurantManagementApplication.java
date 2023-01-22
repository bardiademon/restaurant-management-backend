package com.restaurant.management.restaurantmanagement;

import com.restaurant.management.restaurantmanagement.util.JWTWithUserId;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestaurantManagementApplication
{
    private static JWTWithUserId jwt;

    public static void main(String[] args)
    {
        SpringApplication.run(RestaurantManagementApplication.class , args);
        jwt = new JWTWithUserId();
    }

    public static JWTWithUserId getJwt()
    {
        return jwt;
    }
}
