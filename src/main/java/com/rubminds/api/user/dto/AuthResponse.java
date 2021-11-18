package com.rubminds.api.user.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
        public static Signup build(Long id, String nickname, String job, String introduce) {
            return Signup.builder()
                    .id(id)
                    .nickname(nickname)
                    .job(job)
                    .introduce(introduce)
                    .build();
        }
    }
}
