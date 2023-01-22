package com.restaurant.management.restaurantmanagement.data.repository;

import com.restaurant.management.restaurantmanagement.data.entity.Foods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodsRepository extends JpaRepository<Foods, Long>
{
    @Query(value = "select food from Foods food where food.name like :NAME")
    List<Foods> search(@Param("NAME") final String name);
}
