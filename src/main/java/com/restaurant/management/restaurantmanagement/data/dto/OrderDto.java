package com.restaurant.management.restaurantmanagement.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.restaurant.management.restaurantmanagement.data.entity.Users;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDto(long id , @JsonProperty("user") UsersDto user , @JsonProperty("foods") List<FoodsDto> foods ,
                       @JsonProperty("delivery") UsersDto delivery , @JsonProperty("created_at") LocalDateTime createdAt)
{
}
