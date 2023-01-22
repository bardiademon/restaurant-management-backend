package com.restaurant.management.restaurantmanagement.service;

import com.restaurant.management.restaurantmanagement.data.entity.Users;
import com.restaurant.management.restaurantmanagement.data.repository.UsersRepository;
import com.restaurant.management.restaurantmanagement.util.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.UUID;

@Service
public record UsersService(UsersRepository repository)
{
    @Autowired
    public UsersService
    {
    }

    public Users findUser(final String username , final String password)
    {
        return repository.findByUsernameAndPassword(username , password);
    }

    public Users findUser(final long id)
    {
        return repository.findById(id);
    }

    public Users findUser(final String username)
    {
        return repository.findByUsername(username);
    }

    public Users addNewUser(final Users user , final MultipartFile profilePicture)
    {
        String profilePictureName = null;
        if (profilePicture != null)
        {
            try
            {
                profilePictureName = UUID.randomUUID().toString();
                String contentType = profilePicture.getContentType();

                contentType = contentType == null ? "null" : contentType.replaceAll("/" , "-");

                profilePictureName += "." + contentType + "." + profilePicture.getOriginalFilename();

                final InputStream inputStream = profilePicture.getInputStream();
                final File file = new File(Path.PROFILE_PICTURE + File.separator + profilePictureName);
                System.out.println("file = " + file);
                Files.write(file.toPath() , inputStream.readAllBytes());
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return null;
            }
        }

        user.setProfilePicture(profilePictureName);

        return repository.save(user);
    }
}
