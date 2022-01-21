package com.rubminds.api.chat.domain.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rubminds.api.chat.dto.ChatDto;
import com.rubminds.api.chat.dto.QChatDto_GetChat;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import static com.rubminds.api.chat.domain.QChat.chat;


@RequiredArgsConstructor
public class ChatRepositoryImpl implements ChatRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ChatDto.GetChat> findAllByPostId(Long postId, Pageable pageable) {
        QueryResults<ChatDto.GetChat> result = queryFactory
                .select(new QChatDto_GetChat
                        (chat.id,
                                chat.sender.id,
                                chat.sender.nickname,
                                chat.sender.avatar.url,
                                chat.content,
                                chat.createdAt))
                .from(chat)
                .join(chat.sender)
                .where(chat.post.id.eq(postId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        System.out.println(result.getResults());
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

}






