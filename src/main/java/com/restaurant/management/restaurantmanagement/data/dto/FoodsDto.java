package com.restaurant.management.restaurantmanagement.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public record FoodsDto(long id , String name , int price , String category ,
                       @JsonProperty("created_at") LocalDateTime createdAt)
{
}