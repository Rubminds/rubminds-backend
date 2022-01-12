package com.rubminds.api.message.dto;
import lombok.*;

public class MessageRequest {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Create {
        private Long postId;
        private Long receiverId;
        private String title;
        private String content;
    }
}
