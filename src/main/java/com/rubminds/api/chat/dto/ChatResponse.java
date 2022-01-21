package com.rubminds.api.chat.dto;

import com.rubminds.api.chat.domain.Chat;
import com.rubminds.api.post.domain.Post;
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
        private Long postId;
        private String postTitle;
        private Long writerId;
        private Page<ChatDto.GetChat> chats;

        public static ChatResponse.GetList build(Post post, Page<ChatDto.GetChat> chats) {
            return GetList.builder()
                    .postId(post.getId())
                    .postTitle(post.getTitle())
                    .writerId(post.getWriter().getId())
                    .chats(chats)
                    .build();
        }

    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetPostList {
        private Long postId;
        private String postTitle;

        public static ChatResponse.GetPostList build(Post post) {
            return ChatResponse.GetPostList.builder()
                    .postId(post.getId())
                    .postTitle(post.getTitle())
                    .build();
        }
    }

}
