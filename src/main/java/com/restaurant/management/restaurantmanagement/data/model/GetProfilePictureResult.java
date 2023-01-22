package com.restaurant.management.restaurantmanagement.data.model;

import java.io.File;

public record GetProfilePictureResult(File profilePicture , String filename , String contentType)
{
}
