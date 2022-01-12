package com.rubminds.api.message.dto;

import com.rubminds.api.message.domain.Message;
import com.rubminds.api.post.domain.Post;
import com.rubminds.api.post.dto.PostResponse;
import lombok.*;

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
        private String sender;
        private String receiver;
        private String title;
        private String content;
        private Boolean read;

        public static MessageResponse.Info build(Message message, String sender, String recevier) {
            return Info.builder()
                    .id(message.getId())
                    .postId(message.getPost().getId())
                    .sender(sender)
                    .receiver(recevier)
                    .title(message.getTitle())
                    .content(message.getContent())
                    .read(message.isRead())
                    .build();
        }

    }
}
