package com.rubminds.api.post.dto;

import com.rubminds.api.post.domain.Post;
import com.rubminds.api.post.domain.Kinds;
import com.rubminds.api.post.domain.Meeting;
import com.rubminds.api.post.domain.PostStatus;
import com.rubminds.api.skill.domain.CustomSkill;
import com.rubminds.api.post.domain.PostSkill;
import com.rubminds.api.skill.domain.Skill;
import com.rubminds.api.skill.dto.CustomSkillResponse;
import com.rubminds.api.skill.dto.PostSkillResponse;
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

        public static PostResponse.Info build(Post post, List<Skill> skills) {
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
                    .build();
        }

    }
}
