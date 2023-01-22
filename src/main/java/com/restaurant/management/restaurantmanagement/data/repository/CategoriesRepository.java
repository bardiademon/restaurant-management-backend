package com.restaurant.management.restaurantmanagement.data.repository;

import com.restaurant.management.restaurantmanagement.data.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories, Long>
{
}
