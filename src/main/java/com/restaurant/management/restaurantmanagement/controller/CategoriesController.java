package com.restaurant.management.restaurantmanagement.controller;

import com.restaurant.management.restaurantmanagement.data.dto.ResponseDto;
import com.restaurant.management.restaurantmanagement.data.entity.Categories;
import com.restaurant.management.restaurantmanagement.data.entity.Users;
import com.restaurant.management.restaurantmanagement.data.enums.Response;
import com.restaurant.management.restaurantmanagement.data.enums.Roles;
import com.restaurant.management.restaurantmanagement.data.mapper.CategoriesMapper;
import com.restaurant.management.restaurantmanagement.data.validation.CategoriesValidation;
import com.restaurant.management.restaurantmanagement.data.validation.UsersValidation;
import com.restaurant.management.restaurantmanagement.service.CategoriesService;
import com.restaurant.management.restaurantmanagement.service.UsersService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
                if (CategoriesValidation.nameValidation(categoryName) || userLogged.getRole().equals(Roles.USER))
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

    @GetMapping(
            value = "/list",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseDto<List<String>> list(final HttpServletResponse response , @CookieValue(name = "token") final String token)
    {
        final Users userLogged = UsersValidation.tokenValidation(token , usersService);
        if (userLogged != null)
        {
            if (userLogged.getRole().equals(Roles.ADMIN) || userLogged.getRole().equals(Roles.USER))
            {
                final List<Categories> categories = categoriesService.repository().findAll();
                return new ResponseDto<>(response , CategoriesMapper.toList(categories) , Response.SUCCESSFULLY);
            }
            else return new ResponseDto<>(response , Response.ACCESS_DENIED);
        }
        else return new ResponseDto<>(response , Response.NOT_LOGGED_IN);
    }


    @RequestMapping(
            value = "/{CATEGORY_NAME}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.DELETE
    )
    @ResponseBody
    public ResponseDto<?> remove(final HttpServletResponse response , @PathVariable("CATEGORY_NAME") final String categoryName , @CookieValue(name = "token") final String token)
    {
        final Users userLogged = UsersValidation.tokenValidation(token , usersService);
        if (userLogged != null)
        {
            if (userLogged.getRole().equals(Roles.ADMIN) || userLogged.getRole().equals(Roles.USER))
            {
                if (CategoriesValidation.nameValidation(categoryName))
                {
                    final Categories category = categoriesService.find(categoryName);
                    if (category != null)
                    {
                        categoriesService.repository().delete(category);
                        return new ResponseDto<>(response , Response.SUCCESSFULLY);
                    }
                    else return new ResponseDto<>(response , Response.NOT_FOUND);
                }
                else return new ResponseDto<>(response , Response.INVALID_REQUEST);
            }
            else return new ResponseDto<>(response , Response.ACCESS_DENIED);
        }
        else return new ResponseDto<>(response , Response.NOT_LOGGED_IN);
    }
}
