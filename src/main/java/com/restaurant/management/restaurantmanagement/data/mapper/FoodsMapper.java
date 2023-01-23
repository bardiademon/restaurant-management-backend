package com.restaurant.management.restaurantmanagement.data.mapper;

import com.restaurant.management.restaurantmanagement.data.dto.AddFoodDto;
import com.restaurant.management.restaurantmanagement.data.dto.FoodsDto;
import com.restaurant.management.restaurantmanagement.data.entity.Categories;
import com.restaurant.management.restaurantmanagement.data.entity.Foods;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public final class FoodsMapper
{
    private FoodsMapper()
    {
    }

    public static Foods toFoods(final AddFoodDto addFoodDto , final Categories category)
    {
        final Foods food = new Foods();
        food.setCategory(category);
        food.setName(addFoodDto.name());
        food.setPrice(addFoodDto.price());
        return food;
    }

    public static FoodsDto toFoodDto(final Foods food)
    {
        return new FoodsDto(
                food.getId() , food.getName() , food.getPrice() , food.getCategory().getName() , food.getCreatedAt()
        );
    }

    public static List<FoodsDto> toFoodsDto(final List<Foods> foods)
    {
        final List<FoodsDto> foodsDto = new ArrayList<>();

        if (foods != null && foods.size() > 0)
        {
            for (final Foods food : foods) foodsDto.add(toFoodDto(food));
        }

        return foodsDto;
    }

    public static AddFoodDto toAddFoodDto(final String name , final int price , final String category , final MultipartFile image)
    {
        return new AddFoodDto(name , price , category , image);
    }
}
