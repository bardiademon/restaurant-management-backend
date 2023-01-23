package com.restaurant.management.restaurantmanagement.controller;

import com.restaurant.management.restaurantmanagement.data.dto.AddFoodDto;
import com.restaurant.management.restaurantmanagement.data.dto.FoodsDto;
import com.restaurant.management.restaurantmanagement.data.dto.ResponseDto;
import com.restaurant.management.restaurantmanagement.data.dto.UpdateFoodDto;
import com.restaurant.management.restaurantmanagement.data.entity.Categories;
import com.restaurant.management.restaurantmanagement.data.entity.Foods;
import com.restaurant.management.restaurantmanagement.data.entity.Users;
import com.restaurant.management.restaurantmanagement.data.enums.Response;
import com.restaurant.management.restaurantmanagement.data.enums.Roles;
import com.restaurant.management.restaurantmanagement.data.mapper.FoodsMapper;
import com.restaurant.management.restaurantmanagement.data.model.ImageResult;
import com.restaurant.management.restaurantmanagement.data.validation.FoodsValidation;
import com.restaurant.management.restaurantmanagement.data.validation.UsersValidation;
import com.restaurant.management.restaurantmanagement.service.CategoriesService;
import com.restaurant.management.restaurantmanagement.service.FoodsService;
import com.restaurant.management.restaurantmanagement.service.UsersService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

import static com.restaurant.management.restaurantmanagement.data.validation.UsersValidation.tokenValidation;

@RestController
@RequestMapping(value = "/foods")
public record FoodsController(FoodsService foodsService , UsersService usersService ,
                              CategoriesService categoriesService)
{

    @PostMapping(value = "/add",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseDto<FoodsDto> add
            (final HttpServletResponse response ,
             @RequestParam("name") final String name ,
             @RequestParam("price") final int price ,
             @RequestParam("category") final String categoryStr ,
             @RequestParam("image") final MultipartFile image ,
             @CookieValue(name = "token") final String token)
    {
        final Users userLogged = UsersValidation.tokenValidation(token , usersService);
        if (userLogged != null)
        {
            if (userLogged.getRole().equals(Roles.ADMIN) || userLogged.getRole().equals(Roles.USER))
            {
                final AddFoodDto addFoodDto = FoodsMapper.toAddFoodDto(name , price , categoryStr , image);
                if (FoodsValidation.addValidation(addFoodDto))
                {
                    final Categories category = categoriesService.find(addFoodDto.category());
                    if (category != null)
                    {
                        final Foods food = foodsService.addFood(FoodsMapper.toFoods(addFoodDto , category) , addFoodDto.image());
                        if (food != null)
                        {
                            final FoodsDto foodsDto = FoodsMapper.toFoodDto(food);
                            return new ResponseDto<>(response , foodsDto , Response.SUCCESSFULLY);
                        }
                        else return new ResponseDto<>(response , Response.SERVER_ERROR);
                    }
                    else return new ResponseDto<>(response , Response.NOT_FOUND_CATEGORY);
                }
                else return new ResponseDto<>(response , Response.INVALID_REQUEST);
            }
            else return new ResponseDto<>(response , Response.ACCESS_DENIED);
        }
        else return new ResponseDto<>(response , Response.NOT_LOGGED_IN);
    }

    @GetMapping(value = "/list",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseDto<List<FoodsDto>> list(final HttpServletResponse response , @CookieValue(name = "token") final String token)
    {
        final Users userLogged = UsersValidation.tokenValidation(token , usersService);
        if (userLogged != null)
        {
            if (userLogged.getRole().equals(Roles.ADMIN) || userLogged.getRole().equals(Roles.USER))
            {
                final List<Foods> foods = foodsService.repository().findAll();
                return new ResponseDto<>(response , FoodsMapper.toFoodsDto(foods) , Response.SUCCESSFULLY);
            }
            else return new ResponseDto<>(response , Response.ACCESS_DENIED);
        }
        else return new ResponseDto<>(response , Response.NOT_LOGGED_IN);
    }

    @PostMapping(value = "/search",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseBody
    public ResponseDto<List<FoodsDto>> search(final HttpServletResponse response , @RequestParam("name") final String name , @CookieValue(name = "token") final String token)
    {
        final Users userLogged = UsersValidation.tokenValidation(token , usersService);
        if (userLogged != null)
        {
            if (userLogged.getRole().equals(Roles.ADMIN) || userLogged.getRole().equals(Roles.USER))
            {
                if (FoodsValidation.searchValidation(name))
                {
                    final List<Foods> search = foodsService.search(name);
                    final List<FoodsDto> foods = FoodsMapper.toFoodsDto(search);
                    return new ResponseDto<>(response , foods , Response.SUCCESSFULLY);
                }
                else return new ResponseDto<>(response , Response.INVALID_REQUEST);
            }
            else return new ResponseDto<>(response , Response.ACCESS_DENIED);
        }
        else return new ResponseDto<>(response , Response.NOT_LOGGED_IN);
    }

    @RequestMapping(value = "/delete/{FOOD_ID}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.DELETE
    )
    @ResponseBody
    public ResponseDto<?> delete(final HttpServletResponse response , @PathVariable("FOOD_ID") final String foodIdStr , @CookieValue(name = "token") final String token)
    {
        final Users userLogged = UsersValidation.tokenValidation(token , usersService);
        if (userLogged != null)
        {
            if (userLogged.getRole().equals(Roles.ADMIN) || userLogged.getRole().equals(Roles.USER))
            {
                final Long foodId = FoodsValidation.foodIdValidation(foodIdStr);
                if (foodId != null)
                {
                    final Optional<Foods> byId = foodsService.repository().findById(foodId);
                    if (byId.isPresent())
                    {
                        final Foods food = byId.get();
                        foodsService.deleteFood(food);
                        return new ResponseDto<>(response , Response.SUCCESSFULLY);
                    }
                    else return new ResponseDto<>(response , Response.NOT_FOUND);
                }
                else return new ResponseDto<>(response , Response.INVALID_REQUEST);
            }
            else return new ResponseDto<>(response , Response.ACCESS_DENIED);
        }
        else return new ResponseDto<>(response , Response.NOT_LOGGED_IN);
    }

    @RequestMapping(value = "/update",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.PUT
    )
    @ResponseBody
    public ResponseDto<FoodsDto> update(final HttpServletResponse response , @RequestBody final UpdateFoodDto updateFoodDto , @CookieValue(name = "token") final String token)
    {
        final Users userLogged = UsersValidation.tokenValidation(token , usersService);
        if (userLogged != null)
        {
            if (userLogged.getRole().equals(Roles.ADMIN) || userLogged.getRole().equals(Roles.USER))
            {
                if (FoodsValidation.updateValidation(updateFoodDto))
                {
                    final Optional<Foods> byId = foodsService.repository().findById(updateFoodDto.id());
                    if (byId.isPresent())
                    {
                        Categories category = null;
                        if (updateFoodDto.category() != null && !updateFoodDto.category().isEmpty() && updateFoodDto.category().length() <= 500)
                        {
                            category = categoriesService.find(updateFoodDto.category());
                            if (category == null)
                            {
                                return new ResponseDto<>(response , Response.NOT_FOUND_CATEGORY);
                            }
                        }

                        final Foods food = foodsService.updateFood(byId.get() , updateFoodDto , category);

                        return new ResponseDto<>(response , FoodsMapper.toFoodDto(food) , Response.SUCCESSFULLY);
                    }
                    else return new ResponseDto<>(response , Response.NOT_FOUND);
                }
                else return new ResponseDto<>(response , Response.INVALID_REQUEST);
            }
            else return new ResponseDto<>(response , Response.ACCESS_DENIED);
        }
        else return new ResponseDto<>(response , Response.NOT_LOGGED_IN);
    }

    @RequestMapping(value = "/get-image/{ORDER_ID}",
            produces = MediaType.IMAGE_JPEG_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public byte[] getImage(final HttpServletResponse response , @PathVariable(value = "ORDER_ID") final String orderIdStr , @CookieValue(name = "token") final String token)
    {
        final Users userLogged = tokenValidation(token , usersService);
        if (userLogged != null)
        {
            if (userLogged.getRole().equals(Roles.ADMIN))
            {
                final Long foodId = FoodsValidation.foodIdValidation(orderIdStr);
                if (foodId != null)
                {
                    final Optional<Foods> byId = foodsService.repository().findById(foodId);
                    if (byId.isPresent())
                    {
                        final ImageResult profileImage = foodsService.getImage(byId.get().getOrderImage());
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
        }
        return null;
    }
}
