package com.rubminds.api.skill.dto;

import com.rubminds.api.skill.domain.CustomSkill;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

public class CustomSkillResponse {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetCustomSkill {
        private Long id;
        private String name;

        public static GetCustomSkill build(CustomSkill customSkill){
            return GetCustomSkill.builder()
                    .id(customSkill.getId())
                    .name(customSkill.getName())
                    .build();
        }
    }


    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetCustomSkills {
        private List<GetCustomSkill> skills;
        public static GetCustomSkills build(List<CustomSkill> customSkills){
            return GetCustomSkills.builder()
                    .skills(customSkills.stream().map(GetCustomSkill::build).collect(Collectors.toList()))
                    .build();
        }
    }
}

