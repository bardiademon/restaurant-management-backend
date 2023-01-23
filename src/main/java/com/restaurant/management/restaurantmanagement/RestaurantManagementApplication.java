package com.restaurant.management.restaurantmanagement;

import com.restaurant.management.restaurantmanagement.data.entity.Users;
import com.restaurant.management.restaurantmanagement.data.enums.Roles;
import com.restaurant.management.restaurantmanagement.service.UsersService;
import com.restaurant.management.restaurantmanagement.util.JWTWithUserId;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class RestaurantManagementApplication
{
    private static JWTWithUserId jwt;

    public static void main(String[] args)
    {
        ConfigurableApplicationContext context = SpringApplication.run(RestaurantManagementApplication.class , args);


        jwt = new JWTWithUserId();
        try
        {
            final Users user = new Users();
            user.setName("Bardia");
            user.setUsername("bardiademon");
            user.setAddress("Iran");
            user.setRole(Roles.ADMIN);
            user.setPhone("9170221393");
            user.setPassword("12345");

            final UsersService bean = context.getBean(UsersService.class);

            System.out.println("bean.addNewUser(user , null) = " + bean.addNewUser(user , null));

        }
        catch (Exception e)
        {

        }


    }

    public static JWTWithUserId getJwt()
    {
        return jwt;
    }
}
