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
    public static class CreateOrUpdate {
        private Long id;
        private String nickname;
        private String avatar;

        public static CreateOrUpdate build(User user) {
            CreateOrUpdateBuilder builder = CreateOrUpdate.builder()
                    .id(user.getId())
                    .nickname(user.getNickname());
            if (user.getAvatar() != null) {
                builder.avatar(user.getAvatar().getUrl());
            }
            return builder.build();
        }
    }
}
