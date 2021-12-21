package com.rubminds.api.team.dto;
import lombok.*;

import java.util.List;

public class TeamUserRequest {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Create {
       private Long teamId;
       private Long userId;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Evaluate {
        private Long teamUserId;
        private List<EvaluateData> evaluation;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class EvaluateData {
        private Long teamUserId;
        private Integer attendLevel;
        private Integer workLevel;
    }
}
