package com.restaurant.management.restaurantmanagement.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.restaurant.management.restaurantmanagement.data.entity.Users;

import java.util.List;

public record OrderDto(@JsonProperty("user") UsersDto user , @JsonProperty("foods") List<FoodsDto> foods ,
                       @JsonProperty("delivery") UsersDto delivery)
{
}
