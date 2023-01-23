package com.restaurant.management.restaurantmanagement.data.mapper;

import com.restaurant.management.restaurantmanagement.data.dto.OrderDto;
import com.restaurant.management.restaurantmanagement.data.entity.Orders;

public final class OrdersMapper
{
    private OrdersMapper()
    {
    }

    public static OrderDto toOrdersDto(final Orders order)
    {
        return new OrderDto(UsersMapper.toUserDto(order.getUser()) , FoodsMapper.toFoodsDto(order.getFoods()) , UsersMapper.toUserDto(order.getDelivery()));
    }
}
