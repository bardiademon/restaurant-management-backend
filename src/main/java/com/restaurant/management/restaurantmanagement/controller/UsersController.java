package com.restaurant.management.restaurantmanagement.controller;

import com.restaurant.management.restaurantmanagement.data.dto.ResponseDto;
import com.restaurant.management.restaurantmanagement.data.dto.LoginDto;
import com.restaurant.management.restaurantmanagement.data.entity.Users;
import com.restaurant.management.restaurantmanagement.data.enums.Response;
import com.restaurant.management.restaurantmanagement.service.UsersService;
import com.restaurant.management.restaurantmanagement.util.JWTWithUserId;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import static com.restaurant.management.restaurantmanagement.RestaurantManagementApplication.getJwt;
import static com.restaurant.management.restaurantmanagement.data.validation.UsersValidation.loginValidation;

@RestController
@RequestMapping(value = "/users", method = RequestMethod.POST)
public record UsersController(UsersService usersService)
{

    @Autowired
    public UsersController
    {
    }

    @PostMapping(value = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseDto<String> login(final HttpServletResponse response , @RequestBody final LoginDto loginDto)
    {
        if (loginValidation(loginDto))
        {
            final Users user = usersService.findUser(loginDto.getUsername() , loginDto.getPassword());
            if (user != null)
            {
                final JWTWithUserId jwt = getJwt();
                final String token = jwt.getToken(user.getId());
                response.addCookie(new Cookie("token" , token));
                return new ResponseDto<>(response , token , Response.SUCCESSFULLY);
            }
        }

        return new ResponseDto<>(response , Response.INVALID_LOGIN_INFO);
    }
}
