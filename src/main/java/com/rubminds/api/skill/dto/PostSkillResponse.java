package com.rubminds.api.skill.dto;

import com.rubminds.api.post.domain.Post;
import com.rubminds.api.skill.domain.PostSkill;
import com.rubminds.api.skill.domain.Skill;
import com.rubminds.api.skill.domain.UserSkill;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

public class PostSkillResponse {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetPostSkill {
        private Long postSkillId;
        private String name;

        public static PostSkillResponse.GetPostSkill build(PostSkill postSkill){
            return PostSkillResponse.GetPostSkill.builder()
                    .postSkillId(postSkill.getId())
                    .name(postSkill.getSkill().getName())
                    .build();
        }
    }


    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetPostSkills {
        private List<PostSkillResponse.GetPostSkill> skills;
        public static PostSkillResponse.GetPostSkills build(List<PostSkill> postSkills){
            return PostSkillResponse.GetPostSkills.builder()
                    .skills(postSkills.stream().map(PostSkillResponse.GetPostSkill::build).collect(Collectors.toList()))
                    .build();
        }
    }
}
