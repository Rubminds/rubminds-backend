package com.rubminds.api.team.dto;
import com.rubminds.api.post.domain.Kinds;
import lombok.*;

import java.util.List;

public class TeamUserRequest {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Evaluate {
        private Kinds kinds;
        private List<EvaluateData> evaluation;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class EvaluateData {
        private Long userId;
        private Integer attendLevel;
        private Integer workLevel;
    }
}
