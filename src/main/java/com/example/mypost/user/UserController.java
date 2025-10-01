package com.example.mypost.user;

import com.example.mypost._core.utils.CookieUtil;
import com.example.mypost._core.utils.Define;
import com.example.mypost._core.utils.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

import java.util.Objects;

@Controller
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/user/update-form")
    public String updateForm(@RequestAttribute(Define.LOGIN_USER) LoginUser loginUser,
                             Model model) {
        User user = userService.findById(loginUser.getId());
        model.addAttribute("user", user);
        return "user/update-form";
    }

    @PostMapping("/user/update")
    public String update(UserRequest.UpdateDTO reqDTO,
                         @RequestAttribute(Define.LOGIN_USER) LoginUser loginUser,
                         HttpServletResponse res) {

        reqDTO.validate();
        User updated = userService.updateById(loginUser.getId(), reqDTO);

        boolean needsReissue = !Objects.equals(loginUser.getEmail(), updated.getEmail());

        if (needsReissue) {
            String reissued = JwtUtil.create(updated);
            ResponseCookie cookie = ResponseCookie.from(Define.ACCESS_TOKEN, reissued)
                    .httpOnly(true).secure(false).sameSite(Define.LAX).path("/").maxAge(60 * 60).build();
            res.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        }

        return "redirect:/user/update-form";
    }


    @PostMapping("/user/signup")
    public String signUp(UserRequest.SignUpDTO signUpDTO) {

        signUpDTO.validate();
        userService.signUp(signUpDTO);
        return "redirect:/user/login";

    }

    @GetMapping("/user/login")
    public String loginFrom() {
        log.info("로그인 요첨 폼");
        return "user/login-form";
    }

    @GetMapping("/user/signup")
    public String signUpForm() {
        log.info("회원 가입 요청 폼");
        return "user/signup-form";
    }

    @PostMapping("user/login")
    public String login(UserRequest.LoginDTO loginDTO,
                        HttpServletResponse res) {

        loginDTO.validate();
        log.info("로그인 유효성확인");
        User user = userService.login(loginDTO);

        String token = JwtUtil.create(user);
        res.addHeader(Define.SET_COOKIE, CookieUtil.accessToken(token, false).toString());

        return "redirect:/";

    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse res) {
        res.addHeader(Define.SET_COOKIE, CookieUtil.delete(Define.ACCESS_TOKEN, false).toString());
        return "redirect:/";
    }
}
