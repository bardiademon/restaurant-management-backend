package com.restaurant.management.restaurantmanagement.controller;

import com.restaurant.management.restaurantmanagement.data.dto.ResponseDto;
import com.restaurant.management.restaurantmanagement.data.entity.Categories;
import com.restaurant.management.restaurantmanagement.data.entity.Users;
import com.restaurant.management.restaurantmanagement.data.enums.Response;
import com.restaurant.management.restaurantmanagement.data.enums.Roles;
import com.restaurant.management.restaurantmanagement.data.validation.CategoriesValidation;
import com.restaurant.management.restaurantmanagement.data.validation.UsersValidation;
import com.restaurant.management.restaurantmanagement.service.CategoriesService;
import com.restaurant.management.restaurantmanagement.service.UsersService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/categories", method = {RequestMethod.POST})
public record CategoriesController(CategoriesService categoriesService , UsersService usersService)
{

    @PostMapping(
            value = "/add",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseDto<Long> add(final HttpServletResponse response , @RequestParam(name = "category_name") String categoryName , @CookieValue(name = "token") final String token)
    {
        final Users userLogged = UsersValidation.tokenValidation(token , usersService);
        if (userLogged != null)
        {
            if (userLogged.getRole().equals(Roles.ADMIN))
            {
                if (CategoriesValidation.addValidation(categoryName))
                {
                    if (categoriesService.find(categoryName) == null)
                    {
                        final Categories category = categoriesService.add(categoryName);
                        return new ResponseDto<>(response , category.getId() , Response.SUCCESSFULLY);
                    }
                    else return new ResponseDto<>(response , Response.FOUND);
                }
                else return new ResponseDto<>(response , Response.INVALID_REQUEST);
            }
            else return new ResponseDto<>(response , Response.ACCESS_DENIED);
        }
        else return new ResponseDto<>(response , Response.NOT_LOGGED_IN);
    }
}