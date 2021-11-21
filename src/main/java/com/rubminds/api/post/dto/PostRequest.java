package com.rubminds.api.post.dto;

import com.rubminds.api.post.domain.PostEnumClass.Kinds;
import com.rubminds.api.post.domain.PostEnumClass.Meeting;
import com.rubminds.api.post.domain.PostEnumClass.PostStatus;
import com.rubminds.api.post.domain.PostEnumClass.Region;
import com.rubminds.api.user.domain.User;
import lombok.*;

public class PostRequest {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Create {
        private User user;
        private String title;
        private String content;
        private int headcount;
        private Kinds kinds;
        private Meeting meeting;
        private PostStatus postsStatus;
        private Region region;
    }

    @Setter
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Update {
        private User writer;
        private String title;
        private String content;
        private int headcount;
        private Kinds kinds;
        private Meeting meeting;
        private PostStatus postsStatus;
        private Region region;
    }
}
