package com.rubminds.api.user.dto;

import com.rubminds.api.file.domain.Avatar;
import com.rubminds.api.user.domain.User;
import com.rubminds.api.user.security.token.Token;
import lombok.*;

public class AuthResponse {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Login {
        private Long id;
        private String nickname;
        private String avatar;
        private String accessToken;

        public static Login build(User user, Token accessToken) {
            return Login.builder()
                    .id(user.getId())
                    .nickname(user.getNickname())
                    .avatar(getAvatar(user))
                    .accessToken(accessToken.getToken())
                    .build();
        }

        private static String getAvatar(User user) {
            if (user.getAvatar() != null) {
                return user.getAvatar().getUrl();
            } else {
                return null;
            }
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Signup {
        private Long id;
        private String nickname;
        private String avatar;

        public static Signup build(User user, Avatar avatar) {
            SignupBuilder builder = Signup.builder()
                    .id(user.getId())
                    .nickname(user.getNickname());
            if (avatar != null) {
                builder.avatar(avatar.getUrl());
            }
            return builder.build();
        }
    }
}
