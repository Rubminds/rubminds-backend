package com.rubminds.api.chat.service;

import com.rubminds.api.chat.domain.Chat;
import com.rubminds.api.chat.domain.ChatRoom;
import com.rubminds.api.chat.domain.repository.ChatRoomRepository;
import com.rubminds.api.chat.domain.repository.ChatRoomRepositoryCustom;
import com.rubminds.api.chat.dto.ChatDto;
import com.rubminds.api.common.dto.PageDto;
import com.rubminds.api.chat.domain.repository.ChatRepository;
import com.rubminds.api.chat.dto.ChatRequest;
import com.rubminds.api.chat.dto.ChatResponse;
import com.rubminds.api.post.exception.PostNotFoundException;
import com.rubminds.api.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomRepositoryCustom chatRoomRepositoryCustom;

    @Transactional
    public ChatResponse.OnlyId create(ChatRequest.Create request, User sender) {
        ChatRoom chatRoom = chatRoomRepository.findById(request.getChatRoomId()).orElseThrow(PostNotFoundException::new);
        Chat chat = Chat.create(chatRoom,sender,request.getContent());
        Chat saveChat = chatRepository.save(chat);

        return ChatResponse.OnlyId.build(saveChat);
    }

    public ChatResponse.GetList getChatList(Long chatRoomId, PageDto pageDto) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(PostNotFoundException::new);
        Page<ChatDto.GetChat> chats = chatRepository.findAllByChatRoom(chatRoomId,pageDto.of());
        return ChatResponse.GetList.build(chatRoom, chats);
    }


    public Page<ChatResponse.GetPostList> getPostList(User loginUser, PageDto pageDto) {
        Page<ChatRoom> chatRooms = chatRoomRepositoryCustom.findAllById(loginUser.getId(),pageDto.of());
        return chatRooms.map(chatRoom -> ChatResponse.GetPostList.build(chatRoom));
    }


}
