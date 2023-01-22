package com.restaurant.management.restaurantmanagement.data.validation;

import com.restaurant.management.restaurantmanagement.data.dto.AddFoodDto;
import com.restaurant.management.restaurantmanagement.data.dto.UpdateFoodDto;

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

    public static boolean searchValidation(final String name)
    {
        return (name != null && !name.isEmpty());
    }

    public static boolean updateValidation(final UpdateFoodDto updateFoodDto)
    {
        return (updateFoodDto != null && updateFoodDto.id() > 0);
    }

    public static Long foodIdValidation(final String foodIdStr)
    {
        try
        {
            if (foodIdStr != null && !foodIdStr.isEmpty())
            {
                return Long.parseLong(foodIdStr);
            }
        }
        catch (Exception ignored)
        {
        }

        return null;
    }
}
