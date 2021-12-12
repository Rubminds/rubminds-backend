package com.rubminds.api.team.dto;

import com.rubminds.api.skill.domain.PostSkill;
import com.rubminds.api.skill.dto.PostSkillResponse;
import com.rubminds.api.team.domain.Team;
import lombok.*;

public class TeamResponse {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetTeam {
        private Long teamId;
        private String postTitle;
        private String adminNickname;

        public static TeamResponse.GetTeam build(Team team){
            return GetTeam.builder()
                    .teamId(team.getId())
                    .build();
        }
    }

}
