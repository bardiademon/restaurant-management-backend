package com.restaurant.management.restaurantmanagement.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record AddOrdersDto(@JsonProperty("user_id") long userId , @JsonProperty("foods") List<Long> foodsIds ,
                           @JsonProperty("delivery_id") long deliveryId)
{
    @Override
    public String toString()
    {
        return "AddOrdersDto{" +
                "userId=" + userId +
                ", foodsIds=" + foodsIds +
                ", deliveryId=" + deliveryId +
                '}';
    }
}
