package com.restaurant.management.restaurantmanagement.controller;

import com.restaurant.management.restaurantmanagement.data.dto.AddFoodDto;
import com.restaurant.management.restaurantmanagement.data.dto.FoodsDto;
import com.restaurant.management.restaurantmanagement.data.dto.ResponseDto;
import com.restaurant.management.restaurantmanagement.data.entity.Categories;
import com.restaurant.management.restaurantmanagement.data.entity.Foods;
import com.restaurant.management.restaurantmanagement.data.entity.Users;
import com.restaurant.management.restaurantmanagement.data.enums.Response;
import com.restaurant.management.restaurantmanagement.data.enums.Roles;
import com.restaurant.management.restaurantmanagement.data.mapper.FoodsMapper;
import com.restaurant.management.restaurantmanagement.data.validation.FoodsValidation;
import com.restaurant.management.restaurantmanagement.data.validation.UsersValidation;
import com.restaurant.management.restaurantmanagement.service.CategoriesService;
import com.restaurant.management.restaurantmanagement.service.FoodsService;
import com.restaurant.management.restaurantmanagement.service.UsersService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/foods", method = {RequestMethod.POST , RequestMethod.GET , RequestMethod.DELETE})
public record FoodsController(FoodsService foodsService , UsersService usersService ,
                              CategoriesService categoriesService)
{

    @PostMapping(value = "/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseDto<FoodsDto> add(final HttpServletResponse response , @RequestBody final AddFoodDto addFoodDto , @CookieValue(name = "token") final String token)
    {
        final Users userLogged = UsersValidation.tokenValidation(token , usersService);
        if (userLogged != null)
        {
            if (userLogged.getRole().equals(Roles.ADMIN) || userLogged.getRole().equals(Roles.USER))
            {
                if (FoodsValidation.addValidation(addFoodDto))
                {
                    final Categories category = categoriesService.find(addFoodDto.category());
                    if (category != null)
                    {
                        final Foods food = foodsService.addFood(FoodsMapper.toFoods(addFoodDto , category));
                        final FoodsDto foodsDto = FoodsMapper.toFoodDto(food);
                        return new ResponseDto<>(response , foodsDto , Response.SUCCESSFULLY);
                    }
                    else return new ResponseDto<>(response , Response.NOT_FOUND_CATEGORY);
                }
                else return new ResponseDto<>(response , Response.INVALID_REQUEST);
            }
            else return new ResponseDto<>(response , Response.ACCESS_DENIED);
        }
        else return new ResponseDto<>(response , Response.NOT_LOGGED_IN);
    }

    @GetMapping(value = "/list",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseDto<List<FoodsDto>> list(final HttpServletResponse response , @CookieValue(name = "token") final String token)
    {
        final Users userLogged = UsersValidation.tokenValidation(token , usersService);
        if (userLogged != null)
        {
            if (userLogged.getRole().equals(Roles.ADMIN) || userLogged.getRole().equals(Roles.USER))
            {
                final List<Foods> foods = foodsService.repository().findAll();
                return new ResponseDto<>(response , FoodsMapper.toFoodsDto(foods) , Response.SUCCESSFULLY);
            }
            else return new ResponseDto<>(response , Response.ACCESS_DENIED);
        }
        else return new ResponseDto<>(response , Response.NOT_LOGGED_IN);
    }
}
