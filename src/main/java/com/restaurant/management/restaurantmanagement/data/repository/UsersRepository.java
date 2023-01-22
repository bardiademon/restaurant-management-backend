package com.restaurant.management.restaurantmanagement.data.repository;

import com.restaurant.management.restaurantmanagement.data.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long>
{
}
