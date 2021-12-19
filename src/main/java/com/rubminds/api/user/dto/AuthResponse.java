package com.rubminds.api.user.dto;

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

        public static Login build(Long id, String nickname, String avatar, Token accessToken) {
            return Login.builder()
                    .id(id)
                    .nickname(nickname)
                    .avatar(avatar)
                    .accessToken(accessToken.getToken())
                    .build();
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

        public static Signup build(User user, String avatar) {
            return Signup.builder()
                    .id(user.getId())
                    .nickname(user.getNickname())
                    .avatar(avatar)
                    .build();
        }
    }
}
