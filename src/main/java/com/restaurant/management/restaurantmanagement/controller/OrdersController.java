package com.restaurant.management.restaurantmanagement.controller;

import com.restaurant.management.restaurantmanagement.data.dto.AddOrdersDto;
import com.restaurant.management.restaurantmanagement.data.dto.OrderDto;
import com.restaurant.management.restaurantmanagement.data.dto.ResponseDto;
import com.restaurant.management.restaurantmanagement.data.entity.Foods;
import com.restaurant.management.restaurantmanagement.data.entity.Orders;
import com.restaurant.management.restaurantmanagement.data.entity.Users;
import com.restaurant.management.restaurantmanagement.data.enums.Response;
import com.restaurant.management.restaurantmanagement.data.enums.Roles;
import com.restaurant.management.restaurantmanagement.data.mapper.OrdersMapper;
import com.restaurant.management.restaurantmanagement.data.validation.OrderValidation;
import com.restaurant.management.restaurantmanagement.data.validation.UsersValidation;
import com.restaurant.management.restaurantmanagement.service.FoodsService;
import com.restaurant.management.restaurantmanagement.service.OrderService;
import com.restaurant.management.restaurantmanagement.service.UsersService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/orders")
public record OrdersController(OrderService orderService , UsersService usersService , FoodsService foodsService)
{

    @PostMapping(value = "/add",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseDto<OrderDto> addOrder(final HttpServletResponse response , @RequestBody final AddOrdersDto addOrdersDto , @CookieValue("token") final String token)
    {
        final Users userLogged = UsersValidation.tokenValidation(token , usersService);
        if (userLogged != null)
        {
            if (userLogged.getRole().equals(Roles.ADMIN) || userLogged.getRole().equals(Roles.USER))
            {
                System.out.println(addOrdersDto);
                if (OrderValidation.addOrderValidation(addOrdersDto))
                {
                    final long userId = addOrdersDto.userId();
                    final Users user = usersService.findUser(userId);
                    if (user != null)
                    {
                        final Users delivery = usersService.findUser(addOrdersDto.deliveryId());
                        if (delivery != null)
                        {
                            final List<Long> foodIds = addOrdersDto.foodsIds();
                            final List<Foods> foods = foodsService.repository().findAllById(foodIds);
                            if (foods.size() == foodIds.size())
                            {
                                final Orders order = new Orders();
                                order.setDelivery(delivery);
                                order.setFoods(foods);
                                order.setUser(user);

                                final Orders save = orderService.repository().save(order);

                                return new ResponseDto<>(response , OrdersMapper.toOrdersDto(save) , Response.SUCCESSFULLY);
                            }
                            else return new ResponseDto<>(response , Response.FOODS_NOT_FOUND);
                        }
                        else return new ResponseDto<>(response , Response.DELIVERY_NOT_FOUND);
                    }
                    else return new ResponseDto<>(response , Response.USER_NOT_FOUND);
                }
                else return new ResponseDto<>(response , Response.INVALID_REQUEST);
            }
            else return new ResponseDto<>(response , Response.ACCESS_DENIED);
        }
        else return new ResponseDto<>(response , Response.NOT_LOGGED_IN);
    }
}
