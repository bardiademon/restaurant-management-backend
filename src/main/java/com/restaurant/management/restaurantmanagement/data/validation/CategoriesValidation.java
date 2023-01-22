package com.restaurant.management.restaurantmanagement.data.validation;

public class CategoriesValidation
{
    private CategoriesValidation()
    {

    }

    public static boolean addValidation(final String categoryName)
    {
        return (categoryName != null && !categoryName.isEmpty() && categoryName.length() <= 500);
    }

}

