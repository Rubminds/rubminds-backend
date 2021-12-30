package com.rubminds.api.post.dto;

import com.rubminds.api.post.domain.Kinds;
import com.rubminds.api.post.domain.Meeting;
import com.rubminds.api.post.domain.Post;
import com.rubminds.api.post.domain.PostStatus;
import com.rubminds.api.skill.domain.CustomSkill;
import com.rubminds.api.skill.dto.CustomSkillResponse;
import com.rubminds.api.skill.dto.PostSkillResponse;
import com.rubminds.api.user.security.userdetails.CustomUserDetails;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

public class PostResponse {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class OnlyId {
        private Long id;

        public static PostResponse.OnlyId build(Post post) {
            return PostResponse.OnlyId.builder()
                    .id(post.getId())
                    .build();
        }

    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Info {
        private Long id;
        private String writer;
        private String title;
        private String content;
        private int headcount;
        private String meeting;
        private String postsStatus;
        private String region;
        private List<String> postSkills;
        private List<String> customSkills;
        private Boolean isLike;
        private Long teamId;
        private List<PostDto.File> files;
        private String refLink;
        private String completeContent;
        private Integer finishNum;

        public static PostResponse.Info build(Post post, CustomUserDetails customUserDetails, Integer finishNum) {
            return Info.builder()
                    .id(post.getId())
                    .writer(post.getWriter().getNickname())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .headcount(post.getHeadcount())
                    .meeting(post.getMeeting().name())
                    .postsStatus(post.getPostStatus().name())
                    .region(post.getRegion())
                    .postSkills(post.getPostSkills().stream().map(postSkill -> postSkill.getSkill().getName()).collect(Collectors.toList()))
                    .customSkills(post.getCustomSkills().stream().map(CustomSkill::getName).collect(Collectors.toList()))
                    .isLike(post.isLike(customUserDetails))
                    .teamId(post.getTeam().getId())
                    .files(post.getPostFileList().stream().map(PostDto.File::build).collect(Collectors.toList()))
                    .completeContent(post.getContent())
                    .refLink(post.getRefLink())
                    .finishNum(finishNum)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetPost {
        private Long id;
        private String writer;
        private String title;
        private Kinds kinds;
        private Meeting meeting;
        private PostStatus postStatus;
        private String region;
        private List<PostSkillResponse.GetPostSkill> postSkills;
        private List<CustomSkillResponse.GetCustomSkill> customSkills;
        private boolean postLikeStatus;

        public static PostResponse.GetPost build(Post post, boolean postLikeStatus) {
            return PostResponse.GetPost.builder()
                    .id(post.getId())
                    .writer(post.getWriter().getNickname())
                    .title(post.getTitle())
                    .kinds(post.getKinds())
                    .meeting(post.getMeeting())
                    .postStatus(post.getPostStatus())
                    .region(post.getRegion())
                    .postSkills(post.getPostSkills().stream().map(PostSkillResponse.GetPostSkillByPost::build).collect(Collectors.toList()))
                    .customSkills(post.getCustomSkills().stream().map(CustomSkillResponse.GetCustomSkill::build).collect(Collectors.toList()))
                    .postLikeStatus(postLikeStatus)
                    .build();
        }
    }


    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetList {
        private Long id;
        private String title;
        private String kinds;
        private String status;
        private List<String> skill;
        private String region;
        private Boolean isLike;

        public static GetList build(Post post, CustomUserDetails customUserDetails) {
            return GetList.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .kinds(post.getKinds().name())
                    .region(post.getRegion())
                    .status(post.getPostStatus().name())
                    .skill(post.getPostSkills().stream().map(postSkill -> postSkill.getSkill().getName()).collect(Collectors.toList()))
                    .isLike(post.isLike(customUserDetails))
                    .build();
        }
    }
}

