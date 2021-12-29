package com.rubminds.api.post.dto;

import com.rubminds.api.post.domain.PostFile;
import com.rubminds.api.user.domain.User;
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

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Writer {
        private String nickname;
        private String avatar;

        public static PostDto.Writer build(User user) {
            WriterBuilder builder = Writer.builder().nickname(user.getNickname());
            if (user.getAvatar() != null) {
                builder.avatar(user.getAvatar().getUrl());
            }
            return builder.build();
        }
    }
}
