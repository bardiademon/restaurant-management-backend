package com.restaurant.management.restaurantmanagement.data.validation;

import com.restaurant.management.restaurantmanagement.data.dto.AddFoodDto;

public final class FoodsValidation
{
    private FoodsValidation()
    {
    }

    public static boolean addValidation(final AddFoodDto addFoodDto)
    {
        return (addFoodDto != null &&
                addFoodDto.name() != null && !addFoodDto.name().isEmpty() && addFoodDto.name().length() <= 1000 &&
                addFoodDto.category() != null && !addFoodDto.category().isEmpty() && addFoodDto.category().length() <= 500 &&
                addFoodDto.price() > 0
        );
    }
}
