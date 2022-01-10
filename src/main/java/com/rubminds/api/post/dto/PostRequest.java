package com.rubminds.api.post.dto;

import com.rubminds.api.post.domain.Kinds;
import com.rubminds.api.post.domain.Meeting;
import com.rubminds.api.post.domain.PostStatus;
import lombok.*;

import java.nio.file.WatchEvent.Kind;
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
        private Kinds kinds;
        private Meeting meeting;
        private String region;
        private List<Long> skillIds;
        private List<String> customSkillName;
        private String refLink;
        private String completeContent;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class CreateCompletePost {
        private String refLink;
        private String completeContent;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class ChangeStatus {
        private PostStatus postStatus;
    }
}
