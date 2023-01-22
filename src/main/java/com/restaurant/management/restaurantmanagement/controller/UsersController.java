package com.restaurant.management.restaurantmanagement.controller;

import com.restaurant.management.restaurantmanagement.data.dto.*;
import com.restaurant.management.restaurantmanagement.data.entity.Users;
import com.restaurant.management.restaurantmanagement.data.enums.Response;
import com.restaurant.management.restaurantmanagement.data.enums.Roles;
import com.restaurant.management.restaurantmanagement.data.mapper.UsersMapper;
import com.restaurant.management.restaurantmanagement.data.model.GetProfilePictureResult;
import com.restaurant.management.restaurantmanagement.service.UsersService;
import com.restaurant.management.restaurantmanagement.util.JWTWithUserId;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;

import static com.restaurant.management.restaurantmanagement.RestaurantManagementApplication.getJwt;
import static com.restaurant.management.restaurantmanagement.data.validation.UsersValidation.*;

@RestController
@RequestMapping(value = "/users", method = {RequestMethod.POST , RequestMethod.GET , RequestMethod.PUT})
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

                final Cookie cookie = new Cookie("token" , token);
                cookie.setPath("/");
                response.addCookie(cookie);

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
            (final HttpServletResponse response ,
             @RequestParam final String name ,
             @RequestParam final String username ,
             @RequestParam final String password ,
             @RequestParam final String phone ,
             @RequestParam final String address ,
             @RequestParam(value = "profile_picture") final MultipartFile profilePicture ,
             @RequestParam(name = "role") final String roleStr ,
             @CookieValue(name = "token") final String token)
    {
        final Users userLogged = tokenValidation(token , usersService);
        if (userLogged != null)
        {
            if (userLogged.getRole().equals(Roles.ADMIN))
            {
                final RegisterDto registerDto = UsersMapper.toRegisterDto(name , username , password , phone , address , roleStr , profilePicture);
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
            else return new ResponseDto<>(response , Response.ACCESS_DENIED);
        }
        else return new ResponseDto<>(response , Response.NOT_LOGGED_IN);
    }

    @PostMapping(value = "/search",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseDto<UsersDto> search(final HttpServletResponse response , @RequestParam(name = "username") final String username , @CookieValue(name = "token") final String token)
    {
        final Users userLogged = tokenValidation(token , usersService);
        if (userLogged != null)
        {
            final Users userByUsername = usersService.findUser(username);

            if (userLogged.getRole().equals(Roles.ADMIN) || (userByUsername != null && userByUsername.getUsername().equals(userLogged.getUsername())))
            {
                if (searchValidation(username))
                {
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
            else return new ResponseDto<>(response , Response.ACCESS_DENIED);
        }
        else return new ResponseDto<>(response , Response.NOT_LOGGED_IN);
    }

    @RequestMapping(value = "/update",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT)
    @ResponseBody
    public ResponseDto<UsersDto> update
            (final HttpServletResponse response ,
             @RequestParam(required = false) final String name ,
             @RequestParam(required = false) final String username ,
             @RequestParam(required = false) final String password ,
             @RequestParam(required = false) final String phone ,
             @RequestParam(required = false) final String address ,
             @RequestParam(value = "profile_picture", required = false) final MultipartFile profilePicture ,
             @CookieValue(name = "token") final String token)
    {
        final Users userLogged = tokenValidation(token , usersService);
        if (userLogged != null)
        {
            final UpdateDto updateDto = UsersMapper.toUpdateDto(name , username , password , phone , address , profilePicture);

            if (updateDto.username() != null && !updateDto.username().isEmpty())
            {
                final Users userByUsername = usersService.findUser(updateDto.username());
                if (userByUsername != null)
                {
                    return new ResponseDto<>(response , Response.USERNAME_IS_EXISTS);
                }
            }

            final Users update = usersService.update(userLogged , updateDto);
            return new ResponseDto<>(response , UsersMapper.toUserDto(update) , Response.SUCCESSFULLY);
        }
        else return new ResponseDto<>(response , Response.NOT_LOGGED_IN);
    }

    @RequestMapping(value = "/get-image/{USERNAME}",
            produces = MediaType.IMAGE_JPEG_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public byte[] getImage(final HttpServletResponse response , @PathVariable(value = "USERNAME") final String username , @CookieValue(name = "token") final String token)
    {
        final Users userLogged = tokenValidation(token , usersService);
        if (userLogged != null)
        {
            if (userLogged.getRole().equals(Roles.ADMIN))
            {
                if (getImageValidation(username))
                {
                    final Users user = usersService.findUser(username);

                    final GetProfilePictureResult profileImage = usersService.getProfileImage(user.getProfilePicture());
                    if (profileImage != null)
                    {
                        response.setHeader("Content-Type" , profileImage.contentType());
                        response.setHeader("Content-Disposition" , String.format("form-data; name=\"%s\"" , profileImage.filename()));
                        response.setHeader("Content-Length" , String.valueOf(profileImage.profilePicture().length()));

                        try
                        {
                            return Files.readAllBytes(profileImage.profilePicture().toPath());
                        }
                        catch (IOException ignored)
                        {
                        }
                    }

                }
            }
        }
        return null;
    }
}
