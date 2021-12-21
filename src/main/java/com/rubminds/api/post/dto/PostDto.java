package com.rubminds.api.post.dto;

import com.rubminds.api.post.domain.PostFile;
import com.rubminds.api.skill.domain.Skill;
import lombok.*;

public class PostDto {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class File {
        private String url;

        public static PostDto.File build(PostFile postFile) {
            if (postFile == null) {
                return null;
            }
            return File.builder().url(postFile.getUrl()).build();
        }
    }
}
