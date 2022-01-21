package com.rubminds.api.chat.domain;

import com.rubminds.api.chat.dto.ChatRequest;
import com.rubminds.api.common.domain.BaseEntity;
import com.rubminds.api.post.domain.Post;
import com.rubminds.api.user.domain.User;
import lombok.*;

import javax.persistence.*;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "chat_room")
public class ChatRoom{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public static ChatRoom create(Post post){
        return ChatRoom.builder()
                .post(post)
                .build();
    }
}
