package com.restaurant.management.restaurantmanagement.controller;

import com.restaurant.management.restaurantmanagement.data.dto.RegisterDto;
import com.restaurant.management.restaurantmanagement.data.dto.ResponseDto;
import com.restaurant.management.restaurantmanagement.data.dto.LoginDto;
import com.restaurant.management.restaurantmanagement.data.dto.UsersDto;
import com.restaurant.management.restaurantmanagement.data.entity.Users;
import com.restaurant.management.restaurantmanagement.data.enums.Response;
import com.restaurant.management.restaurantmanagement.data.mapper.UsersMapper;
import com.restaurant.management.restaurantmanagement.service.UsersService;
import com.restaurant.management.restaurantmanagement.util.JWTWithUserId;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.restaurant.management.restaurantmanagement.RestaurantManagementApplication.getJwt;
import static com.restaurant.management.restaurantmanagement.data.validation.UsersValidation.*;

@RestController
@RequestMapping(value = "/users", method = {RequestMethod.POST , RequestMethod.GET})
public record UsersController(UsersService usersService)
{

    @Autowired
    public UsersController
    {
    }

    @PostMapping(value = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseDto<String> login(final HttpServletResponse response , @RequestBody final LoginDto loginDto)
    {
        if (loginValidation(loginDto))
        {
            final Users user = usersService.findUser(loginDto.getUsername() , loginDto.getPassword());
            if (user != null)
            {
                final JWTWithUserId jwt = getJwt();
                final String token = jwt.getToken(user.getId());
                response.addCookie(new Cookie("token" , token));
                return new ResponseDto<>(response , token , Response.SUCCESSFULLY);
            }
        }

        return new ResponseDto<>(response , Response.INVALID_LOGIN_INFO);
    }

    @PostMapping(value = "/register",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseDto<UsersDto> register
            (final HttpServletResponse response , @RequestParam final String name ,
             @RequestParam(required = false) final String username , final @RequestParam(required = false) String password ,
             final @RequestParam(required = false) String phone , final @RequestParam(required = false) String address ,
             @RequestParam(value = "profile_picture") final MultipartFile profilePicture)
    {
        final RegisterDto registerDto = UsersMapper.toRegisterDto(name , username , password , phone , address , profilePicture);
        if (registerValidation(registerDto))
        {
            if (usersService.findUser(registerDto.username()) == null)
            {
                final Users user = usersService.addNewUser(UsersMapper.toUsers(registerDto) , registerDto.profilePicture());
                if (user != null)
                {
                    return new ResponseDto<>(response , UsersMapper.toUserDto(user) , Response.SUCCESSFULLY);
                }
                else return new ResponseDto<>(response , Response.SERVER_ERROR);
            }
            else return new ResponseDto<>(response , Response.USERNAME_IS_EXISTS);
        }

        return new ResponseDto<>(response , Response.INVALID_REQUEST);
    }

    @PostMapping(value = "/search",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseDto<UsersDto> search(final HttpServletResponse response , @RequestParam(name = "username") final String username , @CookieValue(name = "token") final String token)
    {
        final Long id = getJwt().getId(token);
        if (id != null)
        {
            final Users user = usersService.findUser(id);
            if (user != null)
            {
                if (searchValidation(username))
                {
                    final Users userByUsername = usersService.findUser(username);
                    if (userByUsername != null)
                    {
                        return new ResponseDto<>(response , UsersMapper.toUserDto(userByUsername) , Response.SUCCESSFULLY);
                    }
                    else
                    {
                        return new ResponseDto<>(response , Response.NOT_FOUND);
                    }
                }
                else return new ResponseDto<>(response , Response.INVALID_REQUEST);
            }
        }
        return new ResponseDto<>(response , Response.NOT_LOGGED_IN);
    }
}
