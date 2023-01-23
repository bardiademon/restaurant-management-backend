package com.restaurant.management.restaurantmanagement.data.mapper;

import com.restaurant.management.restaurantmanagement.data.dto.OrderDto;
import com.restaurant.management.restaurantmanagement.data.entity.Orders;

import java.util.ArrayList;
import java.util.List;

public final class OrdersMapper
{
    private OrdersMapper()
    {
    }

    public static OrderDto toOrderDto(final Orders order)
    {
        return new OrderDto(order.getId() , UsersMapper.toUserDto(order.getUser()) , FoodsMapper.toFoodsDto(order.getFoods()) , UsersMapper.toUserDto(order.getDelivery()) , order.getCreatedAt());
    }

    public static List<OrderDto> toOrdersDto(final List<Orders> orders)
    {
        final List<OrderDto> ordersDto = new ArrayList<>();
        if (orders != null && orders.size() > 0)
        {
            for (final Orders order : orders) ordersDto.add(toOrderDto(order));
        }

        return ordersDto;
    }
}
