package com.rubminds.api.chat.dto;

import com.rubminds.api.chat.domain.Chat;
import lombok.*;

import java.time.LocalDateTime;

public class ChatDto {

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetChat {
        private Long id;
        private Long senderId;
        private String senderNickname;
        private String avatar;
        private String content;
        private LocalDateTime createAt;

        public static ChatDto.GetChat build(Chat chat) {
            return GetChat.builder()
                    .id(chat.getId())
                    .senderId(chat.getSender().getId())
                    .senderNickname(chat.getSender().getNickname())
                    .content(chat.getContent())
                    .createAt(chat.getCreatedAt())
                    .avatar(chat.getSender().getAvatar()!=null? chat.getSender().getAvatar().getUrl() : null)
                    .build();

        }
    }

}