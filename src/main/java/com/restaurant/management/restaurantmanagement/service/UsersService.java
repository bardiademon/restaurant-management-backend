package com.restaurant.management.restaurantmanagement.service;

import com.restaurant.management.restaurantmanagement.data.dto.UpdateDto;
import com.restaurant.management.restaurantmanagement.data.entity.Users;
import com.restaurant.management.restaurantmanagement.data.model.GetProfilePictureResult;
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
        final String profilePictureName = saveFile(profilePicture);

        if (profilePicture != null && profilePictureName.equals("ERROR")) return null;

        user.setProfilePicture(profilePictureName);

        return repository.save(user);
    }

    private String saveFile(final MultipartFile profilePicture)
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
                return "ERROR";
            }
        }

        return profilePictureName;
    }

    public Users update(final Users userLogged , final UpdateDto updateDto)
    {
        if (updateDto.username() != null && !updateDto.username().isEmpty() && updateDto.username().length() <= 50)
            userLogged.setUsername(updateDto.username());

        if (updateDto.name() != null && !updateDto.name().isEmpty() && updateDto.name().length() <= 50)
            userLogged.setName(updateDto.name());

        if (updateDto.address() != null && !updateDto.address().isEmpty())
            userLogged.setAddress(updateDto.address());

        if (updateDto.phone() != null && !updateDto.phone().isEmpty() && updateDto.phone().length() <= 20)
            userLogged.setPhone(updateDto.phone());

        if (updateDto.password() != null && !updateDto.password().isEmpty() && updateDto.password().length() <= 100)
            userLogged.setPassword(updateDto.password());

        if (updateDto.profilePicture() != null)
        {
            final String profilePictureName = saveFile(updateDto.profilePicture());

            try
            {
                new File(Path.PROFILE_PICTURE + File.separator + userLogged.getProfilePicture()).delete();
                userLogged.setProfilePicture(profilePictureName);
            }
            catch (Exception ignored)
            {
            }
        }

        return repository.save(userLogged);
    }

    public GetProfilePictureResult getProfileImage(String profilePictureName)
    {
        if (profilePictureName != null && !profilePictureName.isEmpty())
        {
            final File file = new File(Path.PROFILE_PICTURE + File.separator + profilePictureName);
            if (file.exists())
            {
                //  2edf8332-8428-4804-9184-9a9d1f596603.image-jpeg.FdrNPgGWIAQKGur.jpg

                profilePictureName = profilePictureName.substring(profilePictureName.indexOf(".") + 1);

                final int pintIndex = profilePictureName.indexOf(".");
                final String contentType = profilePictureName.substring(0 , pintIndex);
                final String filename = profilePictureName.substring(pintIndex + 1);

                return new GetProfilePictureResult(file , filename , contentType);
            }
        }

        return null;
    }
}
