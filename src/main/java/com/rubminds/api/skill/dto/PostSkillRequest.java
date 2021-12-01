package com.rubminds.api.skill.dto;

import com.rubminds.api.post.domain.Post;
import com.rubminds.api.skill.domain.Skill;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PostSkillRequest {
    private Long skill_id;
    private Long post_id;

}
