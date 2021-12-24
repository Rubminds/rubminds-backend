package com.rubminds.api.team.dto;
import com.rubminds.api.team.domain.Team;
import com.rubminds.api.team.domain.TeamUser;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

public class TeamResponse {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetTeam {
        private Long teamId;
        private Long adminId;
        private List<TeamUserResponse.GetTeamUser> teamUsers;
      
        public static TeamResponse.GetTeam build(Team team, List<TeamUser> teamUser){
            return GetTeam.builder()
                    .teamId(team.getId())
                    .adminId(team.getAdmin().getId())
                    .teamUsers(teamUser.stream().map(TeamUserResponse.GetTeamUser::build).collect(Collectors.toList()))
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetAdminId {
        private Long adminId;

        public static TeamResponse.GetAdminId build(Team team){
            return GetAdminId.builder()
                    .adminId(team.getAdmin().getId())
                    .build();
        }
    }

}
