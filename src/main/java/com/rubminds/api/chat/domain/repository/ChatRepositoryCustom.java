package com.rubminds.api.chat.domain.repository;

import com.rubminds.api.chat.domain.ChatRoom;
import com.rubminds.api.chat.dto.ChatDto;
import com.rubminds.api.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ChatRepositoryCustom {
    Page<ChatDto.GetChat> findAllByChatRoom(Long chatRoomId, Pageable pageable);

}
