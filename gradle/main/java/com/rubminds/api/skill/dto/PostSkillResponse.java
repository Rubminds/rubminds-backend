package com.rubminds.api.skill.dto;

import com.rubminds.api.post.domain.Post;
import com.rubminds.api.skill.domain.PostSkill;
import com.rubminds.api.skill.domain.Skill;
import lombok.*;

public class PostSkillResponse {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor()
    public static class addSkill {
        private Long id;
        private String skill;
        private Long post;

        public static addSkill build(PostSkill postSkill) {
            return addSkill.builder()
                    .id(postSkill.getId())
                    .post(postSkill.getPost().getId())
                    .skill(postSkill.getSkill().getName())
                    .build();
        }
    }
}
