package com.rubminds.api.chat.dto;


import lombok.*;
public class ChatRequest {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Create {
        private Long chatRoomId;
        private String content;
    }

}
