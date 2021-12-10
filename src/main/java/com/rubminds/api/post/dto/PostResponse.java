package com.rubminds.api.post.dto;

import com.rubminds.api.post.domain.Post;
import com.rubminds.api.post.domain.PostEnumClass.Kinds;
import com.rubminds.api.post.domain.PostEnumClass.Meeting;
import com.rubminds.api.post.domain.PostEnumClass.PostStatus;
import com.rubminds.api.post.domain.PostEnumClass.Region;
import com.rubminds.api.skill.domain.CostomSkill;
import com.rubminds.api.skill.domain.PostSkill;
import com.rubminds.api.skill.dto.CostomSkillResponse;
import com.rubminds.api.skill.dto.PostSkillResponse;
import com.rubminds.api.user.domain.User;
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
        private Region region;
        private List<PostSkillResponse.GetPostSkill> postSkills;
        private List<CostomSkillResponse.GetCostomSkill> costomSkills;

        public static PostResponse.Info build(Post post, List<PostSkill> postSkills, List<CostomSkill> costomSkills) {
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
                    .postSkills(postSkills.stream().map(PostSkillResponse.GetPostSkill::build).collect(Collectors.toList()))
                    .costomSkills(costomSkills.stream().map(CostomSkillResponse.GetCostomSkill::build).collect(Collectors.toList()))
                    .build();
        }

    }
}
