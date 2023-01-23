package com.restaurant.management.restaurantmanagement.service;

import com.restaurant.management.restaurantmanagement.data.dto.UpdateFoodDto;
import com.restaurant.management.restaurantmanagement.data.entity.Categories;
import com.restaurant.management.restaurantmanagement.data.entity.Foods;
import com.restaurant.management.restaurantmanagement.data.repository.FoodsRepository;
import com.restaurant.management.restaurantmanagement.util.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

@Service
public record FoodsService(FoodsRepository repository)
{
    @Autowired
    public FoodsService
    {
    }

    public Foods addFood(final Foods food , final MultipartFile image)
    {
        final String imageName = saveFile(image);

        if (imageName != null && imageName.equals("ERROR")) return null;

        food.setOrderImage(imageName);

        return repository.save(food);
    }

    private String saveFile(final MultipartFile profilePicture)
    {
        String imageName = null;
        if (profilePicture != null)
        {
            try
            {
                imageName = UUID.randomUUID().toString();
                String contentType = profilePicture.getContentType();

                contentType = contentType == null ? "null" : contentType.replaceAll("/" , "-");

                imageName += "." + contentType + "." + profilePicture.getOriginalFilename();

                final InputStream inputStream = profilePicture.getInputStream();
                final File file = new File(Path.ORDERS_IMAGES + File.separator + imageName);
                System.out.println("file = " + file);
                Files.write(file.toPath() , inputStream.readAllBytes());
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return "ERROR";
            }
        }

        return imageName;
    }

    public Foods updateFood(final Foods food , final UpdateFoodDto updateFoodDto , final Categories category)
    {
        if (category != null) food.setCategory(category);

        if (updateFoodDto.price() > 0) food.setPrice(updateFoodDto.price());

        if (updateFoodDto.name() != null && !updateFoodDto.name().isEmpty())
            food.setName(updateFoodDto.name());

        return repository.save(food);
    }

    public List<Foods> search(final String name)
    {
        return repository.search(String.format("%%%s%%" , name));
    }
}
