package com.restaurant.management.restaurantmanagement.service;

import com.restaurant.management.restaurantmanagement.data.entity.Categories;
import com.restaurant.management.restaurantmanagement.data.repository.CategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public record CategoriesService(CategoriesRepository repository)
{
    @Autowired
    public CategoriesService
    {
    }

    public Categories find(final String name)
    {
        return repository.findByName(name);
    }

    public Categories add(final String categoryName)
    {
        final Categories category = new Categories();
        category.setName(categoryName);
        return repository.save(category);
    }
}
