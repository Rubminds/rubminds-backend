package com.rubminds.api.post.dto;

import com.rubminds.api.post.domain.PostEnumClass.Kinds;
import com.rubminds.api.post.domain.PostEnumClass.Meeting;
import com.rubminds.api.post.domain.PostEnumClass.PostStatus;
import com.rubminds.api.post.domain.PostEnumClass.Region;
import com.rubminds.api.post.domain.Skill;
import com.rubminds.api.user.domain.User;
import lombok.*;

import java.util.List;


@Data
@Getter
@Builder

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UploadPostRequest {
        private User user;
        private String title;
        private String content;
        //private List<Skill> skill;
        private int headcount;
        private Kinds kinds;
        private Meeting meeting;
        private PostStatus postsStatus;
        private Region region;
 }

