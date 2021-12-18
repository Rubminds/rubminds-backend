package com.rubminds.api.post.dto;

import lombok.*;

public class PostLikeRequest {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Update{
        private Long postId;
    }
}
