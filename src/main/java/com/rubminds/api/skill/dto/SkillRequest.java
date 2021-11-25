package com.rubminds.api.skill.dto;

import lombok.*;

import java.util.List;

public class SkillRequest {

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetSkill {
        private Long skillId;
    }
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetSkills {
        private List<Long> skills;
    }
}
