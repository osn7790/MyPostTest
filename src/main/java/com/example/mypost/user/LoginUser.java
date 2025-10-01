package com.example.mypost.user;

import lombok.Builder;
import lombok.Data;

@Data
public class LoginUser {
    private Long id;
    private String username;
    private String email;

    @Builder
    public LoginUser(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    @Builder
    public LoginUser(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }
}
