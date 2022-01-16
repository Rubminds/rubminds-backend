package com.rubminds.api.message.domain;

import com.rubminds.api.common.domain.BaseEntity;
import com.rubminds.api.message.dto.MessageRequest;
import com.rubminds.api.post.domain.Post;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "message")
public class Message extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(nullable = false)
    private Long senderId;

    @Column(nullable = false)
    private Long receiverId;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false, name = "is_read")
    private boolean read;

    public static Message create(MessageRequest.Create request, Post post, Long senderId){
        return Message.builder()
                .post(post)
                .senderId(senderId)
                .receiverId(request.getReceiverId())
                .content(request.getContent())
                .read(false)
                .build();
    }
    public void updateRead(Message message){
        this.read = true;
    }

}
