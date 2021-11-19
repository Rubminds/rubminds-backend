package com.rubminds.api.user.dto;

import com.rubminds.api.user.domain.User;
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

        public static UserResponse.Info build(User user) {
            return UserResponse.Info.builder()
                    .id(user.getId())
                    .nickname(user.getNickname())
                    .job(user.getJob())
                    .introduce(user.getIntroduce())
                    .build();
        }
    }
}
