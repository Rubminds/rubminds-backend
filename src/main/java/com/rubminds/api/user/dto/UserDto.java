package com.rubminds.api.user.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

public class UserDto {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ProjectInfo {
        private String kinds;
        private Long count;

        @Builder
        @QueryProjection
        public ProjectInfo(String kinds, Long count) {
            this.kinds = kinds;
            this.count = count;
        }
    }
}
