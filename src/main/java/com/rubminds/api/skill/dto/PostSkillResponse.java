package com.rubminds.api.skill.dto;

import lombok.*;

public class PostSkillResponse {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class addSkill {
        private Long id;
        private String skill;

        public static addSkill build(Long id, String skill) {
            return addSkill.builder()
                    .id(id)
                    .skill(skill)
                    .build();
        }
    }
}
