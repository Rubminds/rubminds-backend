package com.rubminds.api.chat.domain.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rubminds.api.chat.domain.ChatRoom;
import com.rubminds.api.chat.domain.QChatRoom;
import com.rubminds.api.chat.dto.ChatDto;
import com.rubminds.api.chat.dto.QChatDto_GetChat;
import com.rubminds.api.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static com.rubminds.api.chat.domain.QChat.chat;
import static com.rubminds.api.chat.domain.QChatRoom.chatRoom;
import static com.rubminds.api.user.domain.QUser.user;

@RequiredArgsConstructor
public class ChatRepositoryImpl implements ChatRepositoryCustom, ChatRoomRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ChatDto.GetChat> findAllByChatRoom(Long chatRoomId, Pageable pageable) {
        QueryResults<ChatDto.GetChat> result = queryFactory
                .select(new QChatDto_GetChat
                        (chat.id,
                                chat.sender.id,
                                chat.sender.nickname,
                                chat.sender.avatar.url,
                                chat.content,
                                chat.createdAt))
                .from(chat)
                .join(chat.chatRoom)
                .join(chat.sender)
                .where(chat.chatRoom.id.eq(chatRoomId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        System.out.println(result.getResults());
        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public Page<ChatRoom> findAllById(User loginUser, Pageable pageable) {
        QueryResults<ChatRoom> result = queryFactory.select(chatRoom)
                .from(chatRoom)
                .where(chatRoom.id.eq(
                        JPAExpressions
                        .select(chat.chatRoom.id)
                        .from(chat)
                        .where(chat.sender.eq(loginUser))))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }
}






