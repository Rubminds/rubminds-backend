package com.rubminds.api.skill.dto;

import com.rubminds.api.post.domain.PostSkill;
import com.rubminds.api.skill.domain.Skill;
import lombok.*;

public class PostSkillResponse {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetPostSkill {
        private Long id;
        private String name;

        public static PostSkillResponse.GetPostSkill build(Skill skill){
            return PostSkillResponse.GetPostSkill.builder()
                    .id(skill.getId())
                    .name(skill.getName())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetPostSkillByPost {
        private Long id;
        private String name;

        public static PostSkillResponse.GetPostSkill build(PostSkill postSkill){
            return PostSkillResponse.GetPostSkill.builder()
                    .id(postSkill.getId())
                    .name(postSkill.getSkill().getName())
                    .build();
        }
    }
}