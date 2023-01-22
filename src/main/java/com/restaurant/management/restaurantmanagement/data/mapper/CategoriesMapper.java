package com.restaurant.management.restaurantmanagement.data.mapper;

import com.restaurant.management.restaurantmanagement.data.entity.Categories;

import java.util.ArrayList;
import java.util.List;

public final class CategoriesMapper
{
    private CategoriesMapper()
    {
    }

    public static List<String> toList(final List<Categories> categories)
    {
        final List<String> list = new ArrayList<>();

        if (categories != null && categories.size() > 0)
        {
            for (final Categories category : categories) list.add(category.getName());
        }

        return list;
    }
}
