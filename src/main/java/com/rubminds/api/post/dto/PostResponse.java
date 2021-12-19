package com.rubminds.api.post.dto;

import com.rubminds.api.post.domain.Kinds;
import com.rubminds.api.post.domain.Meeting;
import com.rubminds.api.post.domain.Post;

import com.rubminds.api.post.domain.PostStatus;
import com.rubminds.api.skill.domain.Skill;
import com.rubminds.api.skill.dto.CustomSkillResponse;
import com.rubminds.api.skill.dto.PostSkillResponse;
import com.rubminds.api.team.domain.Team;
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
        private Kinds kinds;
        private Meeting meeting;
        private PostStatus postsStatus;
        private String region;
        private List<PostSkillResponse.GetPostSkill> postSkills;
        private List<CustomSkillResponse.GetCustomSkill> customSkills;
        private Long teamId;

        public static PostResponse.Info build(Post post, List<Skill> skills, Team team) {
            return Info.builder()
                    .id(post.getId())
                    .writer(post.getWriter().getNickname())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .headcount(post.getHeadcount())
                    .kinds(post.getKinds())
                    .meeting(post.getMeeting())
                    .postsStatus(post.getPostStatus())
                    .region(post.getRegion())
                    .postSkills(skills.stream().map(PostSkillResponse.GetPostSkill::build).collect(Collectors.toList()))
                    .customSkills(post.getCustomSkills().stream().map(CustomSkillResponse.GetCustomSkill::build).collect(Collectors.toList()))
                    .teamId(team.getId())
                    .build();
        }

    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetPostLike {
        private boolean postLikeStatus;

        public static PostResponse.GetPostLike build(boolean postLikeStatus) {
            return PostResponse.GetPostLike.builder()
                    .postLikeStatus(postLikeStatus)
                    .build();
        }

    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetPostLike {
        private boolean postLikeStatus;

        public static PostResponse.GetPostLike build(boolean postLikeStatus) {
            return PostResponse.GetPostLike.builder()
                    .postLikeStatus(postLikeStatus)
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
    public static class GetPosts{
        private List<PostResponse.GetPost> posts;

        public static PostResponse.GetPosts build(List<PostResponse.GetPost> posts) {
            return PostResponse.GetPosts.builder()
                    .posts(posts)
                    .build();
        }
    }

}

