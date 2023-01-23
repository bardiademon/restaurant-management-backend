package com.restaurant.management.restaurantmanagement.util;

import java.io.File;

public final class Path
{
    public static final String ROOT = System.getProperty("user.dir");
    public static final String PROFILE_PICTURE = ROOT + File.separator + "profile_pictures";
    public static final String ORDERS_IMAGES = ROOT + File.separator + "orders_images";

    private Path()
    {
    }

}
