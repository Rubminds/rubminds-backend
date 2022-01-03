package com.rubminds.api.team.dto;


import com.rubminds.api.file.domain.Avatar;
import com.rubminds.api.team.domain.TeamUser;
import lombok.*;

public class TeamUserResponse {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class OnlyId {
        private Long id;

        public static TeamUserResponse.OnlyId build(TeamUser teamUser) {
            return TeamUserResponse.OnlyId.builder()
                    .id(teamUser.getId())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetTeamUser {
        private Long teamUserId;
        private Long userId;
        private String userNickname;
        private String userAvatar;
        private boolean admin;
        private boolean finish;

        public static TeamUserResponse.GetTeamUser build(TeamUser teamUser) {
            GetTeamUserBuilder builder = GetTeamUser.builder()
                    .teamUserId(teamUser.getId())
                    .userId(teamUser.getUser().getId())
                    .userNickname(teamUser.getUser().getNickname())
                    .admin(teamUser.getTeam().getAdmin().equals(teamUser.getUser()))
                    .finish(teamUser.isFinish());
            if (teamUser.getUser().getAvatar() != null) {
                builder.userAvatar(teamUser.getUser().getAvatar().getUrl());
            }
            return builder.build();
        }
    }
}
