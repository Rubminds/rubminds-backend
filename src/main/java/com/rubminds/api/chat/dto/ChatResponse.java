package com.rubminds.api.chat.dto;

import com.rubminds.api.chat.domain.Chat;
import com.rubminds.api.chat.domain.ChatRoom;
import lombok.*;
import org.springframework.data.domain.Page;

public class ChatResponse {

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class OnlyId {
        private Long id;

        public static ChatResponse.OnlyId build(Chat chat) {
            return ChatResponse.OnlyId.builder()
                    .id(chat.getId())
                    .build();
        }

    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetList {
        private Long chatRoomId;
        private String postTitle;
        private Long postId;
        private Long writerId;
        private Page<ChatDto.GetChat> chats;

        public static ChatResponse.GetList build(ChatRoom chatRoom, Page<ChatDto.GetChat> chats) {
            return GetList.builder()
                    .chatRoomId(chatRoom.getId())
                    .postId(chatRoom.getPost().getId())
                    .postTitle(chatRoom.getPost().getTitle())
                    .writerId(chatRoom.getPost().getWriter().getId())
                    .chats(chats)
                    .build();
        }

    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetPostList {
        private Long id;
        private Long postId;
        private String postTitle;

        public static ChatResponse.GetPostList build(ChatRoom chatRoom) {
            return ChatResponse.GetPostList.builder()
                    .id(chatRoom.getId())
                    .postId(chatRoom.getPost().getId())
                    .postTitle(chatRoom.getPost().getTitle())
                    .build();
        }
    }

}
