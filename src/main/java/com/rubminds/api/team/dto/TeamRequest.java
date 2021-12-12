package com.rubminds.api.team.dto;
import lombok.*;

public class TeamRequest {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Create {
       private Long postId;
       private Long userId;
    }
}
