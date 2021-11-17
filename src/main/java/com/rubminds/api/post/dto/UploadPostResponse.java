package com.rubminds.api.post.dto;

import com.rubminds.api.post.domain.PostEnumClass.Kinds;
import com.rubminds.api.post.domain.PostEnumClass.Meeting;
import com.rubminds.api.post.domain.PostEnumClass.PostStatus;
import com.rubminds.api.post.domain.PostEnumClass.Region;
import com.rubminds.api.post.domain.Skill;
import com.rubminds.api.user.domain.User;
import lombok.*;

import java.util.List;

public class UploadPostResponse {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)

    public static class Upload {
        private Long id;
        private User user;
        private List<Skill> skill;
        private String title;
        private String content;
        private int headcount;
        private Kinds kinds;
        private Meeting meeting;
        private PostStatus postsStatus;
        private Region region;

        public static UploadPostResponse.Upload build(Long id, User user, String title, String content, int headcount,
                                                      Kinds kinds, Meeting meeting, PostStatus postStatus) {
            return Upload.builder()
                    .id(id)
                    .user(user)
                    .title(title)
                    .content(content)
                    .headcount(headcount)
                    .kinds(kinds)
                    .meeting(meeting)
                    .postsStatus(postStatus)
                    .build();
        }
    }
}


