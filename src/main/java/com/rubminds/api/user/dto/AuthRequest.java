package com.rubminds.api.user.dto;

import lombok.*;

import java.util.List;

public class AuthRequest {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Login {
        private String id;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class Signup {
        private String nickname;
        private String job;
        private String introduce;
        private List<Long> skillIds;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PUBLIC)
    public static class Update {
        private String nickname;
        private String job;
        private String introduce;
        private boolean nicknameChanged;
        private boolean avatarChanged;
        private List<Long> skillIds;
    }
}
