package com.rubminds.api.user.dto;

import com.rubminds.api.skill.dto.UserSkillResponse;
import com.rubminds.api.user.domain.User;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

public class UserResponse {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Info {
        private Long id;
        private String nickname;
        private String job;
        private String introduce;
        private List<UserSkillResponse.GetUserSkill> userSkills;
        private double attendLevel;
        private double workLevel;
        private String avatar;

        public static UserResponse.Info build(User user, String avatarUrl) {
            return UserResponse.Info.builder()
                    .id(user.getId())
                    .nickname(user.getNickname())
                    .job(user.getJob())
                    .introduce(user.getIntroduce())
                    .userSkills(user.getUserSkills().stream().map(UserSkillResponse.GetUserSkill::build).collect(Collectors.toList()))
                    .attendLevel(user.getAttendLevel())
                    .workLevel(user.getWorkLevel())
                    .avatar(avatarUrl)
                    .build();
        }
    }
}
