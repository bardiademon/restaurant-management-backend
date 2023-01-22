package com.restaurant.management.restaurantmanagement.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public final class JWTWithUserId
{

    private final Algorithm algorithm;
    private final JWTVerifier verifier;

    private static final String secret = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALt/GsTv/TZY3hRQSCSgvCHR49RBoj5kMSz7/mc/ATEErTV/HVsROfmFErr57GFQU9YiJDJpSvUncR1cBz2F8DVDQPQU2nKRiJhAVC5aL5hnH/KJhzPySMWmSP+v77TisjM+xW8sJiBqEnhJYhkQlwR41RzdOu1LVJzq7Zvk8TmpAgMBAAECgYAEuTAAdBdJ7jUyB8+5dH8ozvPRXhHrRnTisDp5eWHbTUXbyicAK0/tlEejhDZLQU3LYDLPPk3QHT1BQxc/mocxlS54MpdN+fRUn/C+NgAwnRo9U9c7/82qRfgG7LHS5fssPuSohcvphU+d2weKjMlr+/HzjR40tBDKwj4A9K7FzQJBAPi+IAQEa5ZgxtiWS6b0UQp1LWUoaU5rpV320OWy5ziFTTKdV35emiIYCDGgCVFYwbsnxNNp2H9UAtnQbtBscdsCQQDA94cl2JNEmu+ZdUuPCn7TxiYKY3fVYRF0J95buHKFamvaFoGq68JA+rjeoqZpgDIXxoqTi6Sl77pQQlv0xiPLAkEAlYG5PpDllbI/cmkLuaK5nx6FFXsvqGn9MDgsoRNh9M/ycYyuzQ7Rd9KYAjPdxd4iO3qQzD2fWhDF0eN855t25wJAObUMrq3BBOBMzRi92u4plY2JbMvoV2CrBpCxt75GSU3VKShX6NSOiD4ysSn1GFHlKSUP5iX5vEIf9saRl3b11wJAQ7J33/mE8XOhk0mUGGvF9xc4jKs00FJhNr6g62Lf1hUuO2H/NpngSRZSPa7u3NRx0Zp8cMergIeGTVWatc9QCw==";

    public JWTWithUserId()
    {
        this.algorithm = Algorithm.HMAC256(secret);
        this.verifier = JWT.require(Algorithm.HMAC256(secret)).build();
    }

    public String getToken(final long id)
    {
        return JWT.create()
                .withClaim("user" , id)
                .withExpiresAt(Date.from(LocalDateTime.now().plusDays(1).toInstant(ZoneOffset.UTC)))
                .sign(algorithm);
    }

    public Long getId(final String token)
    {
        try
        {
            return this.verifier.verify(token).getClaim("user").asLong();
        }
        catch (Exception e)
        {
            return null;
        }
    }

}
