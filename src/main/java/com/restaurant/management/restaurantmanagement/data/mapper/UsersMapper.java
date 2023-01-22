package com.restaurant.management.restaurantmanagement.data.mapper;

import com.restaurant.management.restaurantmanagement.data.dto.RegisterDto;
import com.restaurant.management.restaurantmanagement.data.dto.UpdateDto;
import com.restaurant.management.restaurantmanagement.data.dto.UsersDto;
import com.restaurant.management.restaurantmanagement.data.entity.Users;
import com.restaurant.management.restaurantmanagement.data.enums.Roles;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public final class UsersMapper
{
    public static Users toUsers(final RegisterDto registerDto)
    {
        final Users user = new Users();
        user.setUsername(registerDto.username());
        user.setPassword(registerDto.password());
        user.setRole(Roles.valueOf(registerDto.roleStr()));
        user.setAddress(registerDto.address());
        user.setName(registerDto.name());
        user.setPhone(registerDto.phone());
        return user;
    }

    public static List<UsersDto> toUsersDto(final List<Users> users)
    {
        final List<UsersDto> usersDto = new ArrayList<>();
        if (users != null && users.size() > 0)
        {
            for (final Users user : users) usersDto.add(toUserDto(user));
        }

        return usersDto;
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

    public static RegisterDto toRegisterDto(final String name , final String username , final String password , final String phone , final String address , final String roleStr , final MultipartFile profilePicture)
    {
        return new RegisterDto(name , username , password , phone , address , roleStr , profilePicture);
    }

    public static UpdateDto toUpdateDto(final String name , final String username , final String password , final String phone , final String address , final MultipartFile profilePicture)
    {
        return new UpdateDto(name , username , password , phone , address , profilePicture);
    }
}

