package com.restaurant.management.restaurantmanagement.data.dto;

import com.restaurant.management.restaurantmanagement.data.enums.Response;
import jakarta.servlet.http.HttpServletResponse;

public final class ResponseDto<INFO>
{
    private final INFO info;
    private final Response message;

    public ResponseDto(final HttpServletResponse response , final Response message)
    {
        this(response , null , message);
    }

    public ResponseDto(final HttpServletResponse response , final INFO info , final Response message)
    {
        response.setStatus(message.statusCode);
        this.info = info;
        this.message = message;
    }

    public INFO getInfo()
    {
        return info;
    }


    public Response getMessage()
    {
        return message;
    }

}
