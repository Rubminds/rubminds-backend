package com.rubminds.api.chat.domain.repository;

import com.rubminds.api.chat.domain.ChatRoom;
import com.rubminds.api.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ChatRoomRepositoryCustom {
    Page<ChatRoom> findAllById(User loginUser, Pageable pageable);

}
