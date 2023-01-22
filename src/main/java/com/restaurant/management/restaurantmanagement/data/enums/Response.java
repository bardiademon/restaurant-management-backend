package com.restaurant.management.restaurantmanagement.data.enums;

import jakarta.servlet.http.HttpServletResponse;

public enum Response
{
    INVALID_LOGIN_INFO(HttpServletResponse.SC_BAD_REQUEST),
    INVALID_REQUEST(HttpServletResponse.SC_BAD_REQUEST),
    USERNAME_IS_EXISTS(HttpServletResponse.SC_BAD_REQUEST),
    SUCCESSFULLY(HttpServletResponse.SC_OK)
    //
    ;

    public final int statusCode;

    Response(final int statusCode)
    {
        this.statusCode = statusCode;
    }
}
