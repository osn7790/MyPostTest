package com.example.mypost._core.interceptor;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.mypost._core.errors.exception.Exception401;
import com.example.mypost._core.errors.exception.Exception500;
import com.example.mypost._core.utils.Define;
import com.example.mypost._core.utils.JwtUtil;
import com.example.mypost.user.LoginUser;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        log.debug("====== JWT 인증 인터셉터 시작 ====");

        String jwt = resolveToken(request);
//                = request.getHeader("Authorization");

        if(jwt == null || jwt.isBlank()) {
            throw new Exception401("JWT 토큰을 전달해주세요");
        }
        jwt = jwt.replace("Bearer ", "");

        try {
            LoginUser loginUser = JwtUtil.verify(jwt);
            request.setAttribute(Define.LOGIN_USER, loginUser);
            return true;

        } catch (TokenExpiredException e) {
            throw new Exception401("토큰 만료 시간이 지났어요. 다시 로그인해주세요");
        } catch (JWTDecodeException e) {
            throw new Exception401("토큰이 유효하지 않습니다");
        } catch (Exception e) {
            throw new Exception500(e.getMessage());
        }
    }

    private String resolveToken(HttpServletRequest req) {
        String auth = req.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            return auth.substring(7);
        }

        Cookie [] cookies = req.getCookies();
        if (cookies != null) {
            for(Cookie c : cookies) {
                if (Define.ACCESS_TOKEN.equals(c.getName())) {
                    return c.getValue();
                }
            }

        }
        return null;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
