package com.example.mypost.post;

import com.example.mypost.user.LoginUser;
import com.example.mypost.user.User;
import lombok.Data;

public class PostRequest {

    @Data
    public static class SaveDTO {
        private String title;
        private String content;

        public Post toEntity(User user) {
            return Post.builder()
                    .title(this.title)
                    .user(user)
                    .content(this.content)
                    .build();
        }

        public void validate() {
            if(title == null || title.trim().isEmpty()){
                throw new IllegalArgumentException("제목은 필수입니다");
            }

            if(content == null || content.trim().isEmpty()){
                throw new IllegalArgumentException("내용은 필수입니다");
            }

        }
    }

    @Data
    public static class UpdateDTO {
        private String title;
        private String content;

        public void validate() {
            if(title == null || title.trim().isEmpty()){
                throw new IllegalArgumentException("제목은 필수입니다");
            }

            if(content == null || content.trim().isEmpty()){
                throw new IllegalArgumentException("내용은 필수입니다");
            }

        }


    }


}
