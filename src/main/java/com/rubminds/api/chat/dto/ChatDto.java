package com.rubminds.api.chat.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import java.time.LocalDateTime;

public class ChatDto {

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetChat {
        private Long id;
        private Long senderId;
        private String senderNickname;
        private String avatar;
        private String content;
        private LocalDateTime createAt;

        @Builder
        @QueryProjection
        public GetChat(Long id, Long senderId, String senderNickname, String avatar, String content, LocalDateTime createAt) {
            this.id = id;
            this.senderId = senderId;
            this.senderNickname = senderNickname;
            this.content = content;
            this.createAt = createAt;

            if (avatar != null) {
                this.avatar = avatar;
            }
        }
    }
}