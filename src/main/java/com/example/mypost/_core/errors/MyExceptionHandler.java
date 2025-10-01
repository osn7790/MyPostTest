package com.example.mypost._core.errors;

import com.example.mypost._core.errors.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Order
@ControllerAdvice
public class MyExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(MyExceptionHandler.class);

    @ExceptionHandler(Exception400.class)
    public String ex400(Exception400 e, HttpServletRequest req, Model model) {
        log.warn("400 Bad Request: url={}, ua={}", req.getRequestURL(), req.getHeader("User-Agent"));
        model.addAttribute("status", 400);
        model.addAttribute("message", e.getMessage());
        model.addAttribute("path", req.getRequestURL());
        return "error/400";

    }

    @ExceptionHandler(Exception401.class)
    public String ex401(Exception401 e, HttpServletRequest req, Model model) {
        log.warn("401 UnAuthorized: url={}, ua={}", req.getRequestURL(), req.getHeader("User-Agent"));
        model.addAttribute("status", 401);
        model.addAttribute("message", e.getMessage());
        model.addAttribute("path", req.getRequestURL());
        return "error/401";

    }

    @ExceptionHandler(Exception403.class)
    public String ex403(Exception403 e, HttpServletRequest req, Model model) {
        log.warn("403 Forbidden: url={}, ua={}", req.getRequestURL(), req.getHeader("User-Agent"));
        model.addAttribute("status", 403);
        model.addAttribute("message", e.getMessage());
        model.addAttribute("path", req.getRequestURL());
        return "error/403";

    }

    @ExceptionHandler(Exception404.class)
    public String ex400(Exception404 e, HttpServletRequest req, Model model) {
        log.warn("404 Not Found: url={}, ua={}", req.getRequestURL(), req.getHeader("User-Agent"));
        model.addAttribute("status", 404);
        model.addAttribute("message", e.getMessage());
        model.addAttribute("path", req.getRequestURL());
        return "error/404";

    }

    @ExceptionHandler(Exception500.class)
    public String ex500(Exception500 e, HttpServletRequest req, Model model) {
        log.warn("500: url={}, ua={}", req.getRequestURL(), req.getHeader("User-Agent"), e);
        model.addAttribute("status", 500);
        model.addAttribute("message", e.getMessage());
        model.addAttribute("path", req.getRequestURL());
        return "error/500";

    }


}
