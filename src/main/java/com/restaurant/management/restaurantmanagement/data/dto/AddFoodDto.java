package com.restaurant.management.restaurantmanagement.data.dto;

import org.springframework.web.multipart.MultipartFile;

public record AddFoodDto(String name , int price , String category , MultipartFile image)
{
}
