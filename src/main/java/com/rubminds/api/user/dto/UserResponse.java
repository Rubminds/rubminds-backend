package com.rubminds.api.user.dto;

import lombok.*;

public class UserResponse {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Info {
        private Long id;
        private String nickname;
        private String job;
        private String introduce;
        public static UserResponse.Info build(Long id, String nickname, String job, String introduce) {
            return UserResponse.Info.builder()
                    .id(id)
                    .nickname(nickname)
                    .job(job)
                    .introduce(introduce)
                    .build();
        }
    }
}
