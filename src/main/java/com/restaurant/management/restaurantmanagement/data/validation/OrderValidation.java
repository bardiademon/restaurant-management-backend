package com.restaurant.management.restaurantmanagement.data.validation;

import com.restaurant.management.restaurantmanagement.data.dto.AddOrdersDto;

public final class OrderValidation
{
    private OrderValidation()
    {
    }

    public static boolean addOrderValidation(final AddOrdersDto addOrdersDto)
    {
        return (
                addOrdersDto != null && addOrdersDto.userId() > 0 && addOrdersDto.deliveryId() > 0 && addOrdersDto.foodsIds() != null && addOrdersDto.foodsIds().size() > 0
        );
    }
}
