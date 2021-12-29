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
        private Boolean isMine;
        private List<UserDto.ProjectInfo> projectInfo;
        private List<UserDto.LikeInfo> likeInfo;

        public static UserResponse.Info build(User user, User loginUser, List<UserDto.ProjectInfo> projectInfos, List<UserDto.LikeInfo> likeInfo) {
            InfoBuilder builder = Info.builder()
                    .id(user.getId())
                    .nickname(user.getNickname())
                    .job(user.getJob())
                    .introduce(user.getIntroduce())
                    .userSkills(user.getUserSkills().stream().map(UserSkillResponse.GetUserSkill::build).collect(Collectors.toList()))
                    .attendLevel(user.getAttendLevel())
                    .isMine(user.isMine(loginUser))
                    .projectInfo(projectInfos)
                    .likeInfo(likeInfo)
                    .workLevel(user.getWorkLevel());
            if (user.getAvatar() != null) {
                builder.avatar(user.getAvatar().getUrl());
            }
            return builder.build();
        }
    }
}
