package com.rubminds.api.post.dto;

import com.rubminds.api.post.domain.Meeting;
import lombok.*;

import java.util.List;

public class PostRequest {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class CreateOrUpdate {
        private String title;
        private String content;
        private int headcount;
        private Meeting meeting;
        private String region;
        private List<Long> skillIds;
        private List<String> customSkillName;
    }
}
