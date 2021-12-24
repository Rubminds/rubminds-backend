package com.rubminds.api.team.dto;
import lombok.*;

public class TeamUserRequest {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class CreateAndDelete {
       private Long team_id;
    }
}
