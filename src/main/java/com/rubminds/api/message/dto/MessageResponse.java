package com.rubminds.api.message.dto;

import com.rubminds.api.message.domain.Message;
import com.rubminds.api.post.domain.Post;
import com.rubminds.api.post.dto.PostResponse;
import lombok.*;

import java.time.LocalDateTime;

public class MessageResponse {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class OnlyId {
        private Long id;

        public static MessageResponse.OnlyId build(Message message) {
            return MessageResponse.OnlyId.builder()
                    .id(message.getId())
                    .build();
        }

    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Info {
        private Long id;
        private Long postId;
        private String postTitle;
        private String sender;
        private String content;
        private LocalDateTime createAt;

        public static MessageResponse.Info build(Message message, String sender, String recevier) {
            return Info.builder()
                    .id(message.getId())
                    .postId(message.getPost().getId())
                    .postTitle(message.getPost().getTitle())
                    .sender(sender)
                    .content(message.getContent())
                    .createAt(message.getCreatedAt())
                    .build();
        }

    }
}
