package com.rubminds.api.skill.dto;

import com.rubminds.api.skill.domain.Skill;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

public class SkillResponse {

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetSkill {
        private Long id;
        private String name;

        public static GetSkill build(Skill skill){
            return SkillResponse.GetSkill.builder()
                    .id(skill.getId())
                    .name(skill.getName())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetSkills {
        private List<GetSkill> skills;
        public static GetSkills build(List<Skill> skills){
            return GetSkills.builder()
                    .skills(skills.stream().map(GetSkill::build).collect(Collectors.toList()))
                    .build();
        }
    }
}
