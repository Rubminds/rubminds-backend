package com.rubminds.api.team.dto;
import com.rubminds.api.team.domain.TeamUser;
import lombok.*;

import java.util.List;

public class TeamRequest {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Create {
       private Long postId;
       private Long userId;
       private List<String> members;
    }
}
