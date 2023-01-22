package com.restaurant.management.restaurantmanagement.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.multipart.MultipartFile;

public record RegisterDto(String name , String username , String password , String phone , String address , String roleStr , @JsonProperty("profile_picture") MultipartFile profilePicture)
{
}
