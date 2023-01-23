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
import java.util.Optional;

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

                                return new ResponseDto<>(response , OrdersMapper.toOrderDto(save) , Response.SUCCESSFULLY);
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

    @GetMapping(value = "/{ORDER_ID}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseDto<OrderDto> findById(final HttpServletResponse response , @PathVariable("ORDER_ID") final String orderIdStr , @CookieValue("token") final String token)
    {
        final Users userLogged = UsersValidation.tokenValidation(token , usersService);
        if (userLogged != null)
        {
            if (userLogged.getRole().equals(Roles.ADMIN) || userLogged.getRole().equals(Roles.USER))
            {
                final Long orderId = OrderValidation.findByIdOrderValidation(orderIdStr);
                if (orderId != null)
                {
                    final Optional<Orders> findById = orderService.repository().findById(orderId);
                    if (findById.isPresent())
                    {
                        final Orders order = findById.get();
                        return new ResponseDto<>(response , OrdersMapper.toOrderDto(order) , Response.SUCCESSFULLY);
                    }
                    else return new ResponseDto<>(response , Response.NOT_FOUND);
                }
                else return new ResponseDto<>(response , Response.INVALID_REQUEST);
            }
            else return new ResponseDto<>(response , Response.ACCESS_DENIED);
        }
        else return new ResponseDto<>(response , Response.NOT_LOGGED_IN);
    }

    @GetMapping(value = "/user/{USER_ID}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseDto<List<OrderDto>> findByUserId(final HttpServletResponse response , @PathVariable("USER_ID") final String userIdStr , @CookieValue("token") final String token)
    {
        final Users userLogged = UsersValidation.tokenValidation(token , usersService);
        if (userLogged != null)
        {
            if (userLogged.getRole().equals(Roles.ADMIN) || userLogged.getRole().equals(Roles.USER))
            {
                final Long userId = OrderValidation.findByIdOrderValidation(userIdStr);
                if (userId != null)
                {
                    final List<Orders> orders = orderService.find(userId);
                    if (orders != null)
                    {
                        return new ResponseDto<>(response , OrdersMapper.toOrdersDto(orders) , Response.SUCCESSFULLY);
                    }
                    else return new ResponseDto<>(response , Response.NOT_FOUND);
                }
                else return new ResponseDto<>(response , Response.INVALID_REQUEST);
            }
            else return new ResponseDto<>(response , Response.ACCESS_DENIED);
        }
        else return new ResponseDto<>(response , Response.NOT_LOGGED_IN);
    }

    @RequestMapping(value = "/{ORDER_ID}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.DELETE
    )
    @ResponseBody
    public ResponseDto<?> delete(final HttpServletResponse response , @PathVariable("ORDER_ID") final String orderIdStr , @CookieValue("token") final String token)
    {
        final Users userLogged = UsersValidation.tokenValidation(token , usersService);
        if (userLogged != null)
        {
            if (userLogged.getRole().equals(Roles.ADMIN) || userLogged.getRole().equals(Roles.USER))
            {
                final Long orderId = OrderValidation.findByIdOrderValidation(orderIdStr);
                if (orderId != null)
                {
                    final Optional<Orders> byId = orderService.repository().findById(orderId);
                    if (byId.isPresent())
                    {
                        orderService.repository().delete(byId.get());
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
