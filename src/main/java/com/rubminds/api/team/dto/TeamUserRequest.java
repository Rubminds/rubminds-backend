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
    public static class EvaluateProject {
        private Long teamUserId;
        private List<EvaluateProjectData> evaluation;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class EvaluateStudy {
        private Long teamUserId;
        private List<EvaluateStudyData> evaluation;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class EvaluateProjectData {
        private Long teamUserId;
        private Integer attendLevel;
        private Integer workLevel;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class EvaluateStudyData {
        private Long teamUserId;
        private Integer attendLevel;
    }
}
