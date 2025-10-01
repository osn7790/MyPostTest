package com.example.mypost.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

public class UserRequest {

    private static final Pattern PWD_MING = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\\\d)\\\\S{6,}$");

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignUpDTO {

        ;

        private String username;
        private String password;
        private String email;

        public User toEntity() {
            return User.builder()
                    .username(this.username)
                    .password(this.password)
                    .email(this.email)
                    .build();
        }

        public void validate() {

            if(username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("사용자 명은 필수입니다");
            }
            if(password == null || PWD_MING.matcher(password).matches()) {
                throw new IllegalArgumentException("영문/숫자 포함, 공백 없이 6자 이상 필수입니다");
            }
            if(email.contains("@") == false) {
                throw new IllegalArgumentException("올바른 이메일 형식이 아닙니다");
            }
        }

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginDTO {
        private String username;
        private String password;

        public void validate() {

            if(username == null || username.trim().isEmpty()) {
                throw new IllegalArgumentException("사용자 명은 필수입니다");
            }
            if(password == null || PWD_MING.matcher(password).matches()) {
                throw new IllegalArgumentException("영문/숫자 포함, 공백 없이 6자 이상 필수입니다");
            }

        }

    }

    @Data
    public static class UpdateDTO{
        private String password;
        private String email;

        public void validate() {

            if(password == null || PWD_MING.matcher(password).matches()) {
                throw new IllegalArgumentException("영문/숫자 포함, 공백 없이 6자 이상 필수입니다");
            }
            if(email.contains("@") == false) {
                throw new IllegalArgumentException("올바른 이메일 형식이 아닙니다");
            }

        }

    }



}
