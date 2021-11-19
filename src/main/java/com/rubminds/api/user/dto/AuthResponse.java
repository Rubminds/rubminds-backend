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
        private String accessToken;

        public static Login build(Long id, String nickname, Token accessToken) {
            return Login.builder()
                    .id(id)
                    .nickname(nickname)
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
        private String job;
        private String introduce;

        public static Signup build(User user) {
            return Signup.builder()
                    .id(user.getId())
                    .nickname(user.getNickname())
                    .job(user.getJob())
                    .introduce(user.getIntroduce())
                    .build();
        }
    }
}
