package com.restaurant.management.restaurantmanagement.data.mapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.restaurant.management.restaurantmanagement.data.dto.RegisterDto;
import com.restaurant.management.restaurantmanagement.data.dto.UsersDto;
import com.restaurant.management.restaurantmanagement.data.entity.Users;
import org.springframework.web.multipart.MultipartFile;

public final class UsersMapper
{
    public static Users toUsers(final RegisterDto registerDto)
    {
        final Users user = new Users();
        user.setUsername(registerDto.username());
        user.setPassword(registerDto.password());
        user.setAddress(registerDto.address());
        user.setName(registerDto.name());
        user.setPhone(registerDto.phone());

        return user;
    }

    public static UsersDto toUserDto(final Users user)
    {
        final UsersDto usersDto = new UsersDto();
        usersDto.setId(user.getId());
        usersDto.setUsername(user.getUsername());
        usersDto.setAddress(user.getAddress());
        usersDto.setName(user.getName());
        usersDto.setPhone(user.getPhone());
        usersDto.setCreatedAt(user.getCreatedAt());

        return usersDto;
    }

    public static RegisterDto toRegisterDto(String name , String username , String password , String phone , String address , MultipartFile profilePicture)
    {
        return new RegisterDto(name , username , password , phone , address , profilePicture);
    }
}

