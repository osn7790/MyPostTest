package com.example.mypost._core.utils;

import org.springframework.http.ResponseCookie;

public class CookieUtil {

    public static ResponseCookie accessToken(String token, boolean prod) {
        return ResponseCookie.from(Define.ACCESS_TOKEN, token)
                .httpOnly(true)
                .secure(prod)
                .sameSite(prod ? Define.LAX : Define.LAX)
                .path("/")
                .maxAge(60 * 60)
                .build();
    }

    public static ResponseCookie delete(String name, boolean prod) {
        return ResponseCookie.from(name, "")
                .httpOnly(true)
                .secure(prod)
                .sameSite(Define.LAX)
                .path("/")
                .maxAge(0)
                .build();
    }


}
