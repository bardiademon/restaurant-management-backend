package com.restaurant.management.restaurantmanagement.service;

import com.restaurant.management.restaurantmanagement.data.dto.UpdateFoodDto;
import com.restaurant.management.restaurantmanagement.data.entity.Categories;
import com.restaurant.management.restaurantmanagement.data.entity.Foods;
import com.restaurant.management.restaurantmanagement.data.repository.FoodsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public record FoodsService(FoodsRepository repository)
{
    @Autowired
    public FoodsService
    {
    }

    public Foods addFood(final Foods food)
    {
        return repository.save(food);
    }

    public Foods updateFood(final Foods food , final UpdateFoodDto updateFoodDto , final Categories category)
    {
        if (category != null) food.setCategory(category);

        if (updateFoodDto.price() > 0) food.setPrice(updateFoodDto.price());

        if (updateFoodDto.name() != null && !updateFoodDto.name().isEmpty())
            food.setName(updateFoodDto.name());

        return repository.save(food);
    }
}
