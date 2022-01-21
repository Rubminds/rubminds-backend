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
@Table(name = "chat")
public class Chat extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    @Column(nullable = false)
    private String content;


    public static Chat create(Post post, User sender, String content){
        return Chat.builder()
                .post(post)
                .sender(sender)
                .content(content)
                .build();
    }

}
