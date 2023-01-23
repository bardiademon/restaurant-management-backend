package com.restaurant.management.restaurantmanagement.data.enums;

import jakarta.servlet.http.HttpServletResponse;

public enum Response
{
    INVALID_LOGIN_INFO(HttpServletResponse.SC_BAD_REQUEST),
    INVALID_REQUEST(HttpServletResponse.SC_BAD_REQUEST),
    FOUND(HttpServletResponse.SC_BAD_REQUEST),
    NOT_FOUND_CATEGORY(HttpServletResponse.SC_BAD_REQUEST),
    USERNAME_IS_EXISTS(HttpServletResponse.SC_BAD_REQUEST),
    USER_NOT_FOUND(HttpServletResponse.SC_BAD_REQUEST),
    DELIVERY_NOT_FOUND(HttpServletResponse.SC_BAD_REQUEST),
    FOODS_NOT_FOUND(HttpServletResponse.SC_BAD_REQUEST),
    ACCESS_DENIED(HttpServletResponse.SC_BAD_REQUEST),
    SERVER_ERROR(HttpServletResponse.SC_INTERNAL_SERVER_ERROR),
    NOT_LOGGED_IN(HttpServletResponse.SC_INTERNAL_SERVER_ERROR),
    NOT_FOUND(HttpServletResponse.SC_OK),
    SUCCESSFULLY(HttpServletResponse.SC_OK)
    //
    ;

    public final int statusCode;

    Response(final int statusCode)
    {
        this.statusCode = statusCode;
    }
}
