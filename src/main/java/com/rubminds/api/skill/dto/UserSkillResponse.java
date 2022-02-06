package com.rubminds.api.skill.dto;

import com.rubminds.api.skill.domain.UserSkill;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

public class UserSkillResponse {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetUserSkill {
        private Long id;
        private String name;
        private String url;

        public static UserSkillResponse.GetUserSkill build(UserSkill userSkill){
            return UserSkillResponse.GetUserSkill.builder()
                    .id(userSkill.getId())
                    .name(userSkill.getSkill().getName())
                    .url(userSkill.getSkill().getUrl())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetUserSkills {
        private List<UserSkillResponse.GetUserSkill> skills;
        public static UserSkillResponse.GetUserSkills build(List<UserSkill> userSkills){
            return UserSkillResponse.GetUserSkills.builder()
                    .skills(userSkills.stream().map(UserSkillResponse.GetUserSkill::build).collect(Collectors.toList()))
                    .build();
        }
    }
}
