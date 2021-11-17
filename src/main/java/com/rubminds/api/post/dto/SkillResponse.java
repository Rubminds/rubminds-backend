package com.rubminds.api.post.dto;

import com.rubminds.api.post.domain.PostEnumClass.Kinds;
import com.rubminds.api.post.domain.PostEnumClass.Meeting;
import com.rubminds.api.post.domain.PostEnumClass.PostStatus;
import com.rubminds.api.post.domain.PostEnumClass.Region;
import com.rubminds.api.post.domain.Skill;
import com.rubminds.api.user.domain.User;
import lombok.*;

import java.util.List;

public class SkillResponse {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class addSkill {
        private Long id;
        private String skill;

        public static addSkill build(Long id, String skill) {
            return addSkill.builder()
                    .id(id)
                    .skill(skill)
                    .build();
        }
    }
}
