package com.restaurant.management.restaurantmanagement.data.validation;

public class CategoriesValidation
{
    private CategoriesValidation()
    {

    }

    public static boolean nameValidation(final String categoryName)
    {
        return (categoryName != null && !categoryName.isEmpty() && categoryName.length() <= 500);
    }

}

