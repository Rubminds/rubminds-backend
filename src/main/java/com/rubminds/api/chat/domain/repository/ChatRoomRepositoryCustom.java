package com.rubminds.api.chat.domain.repository;
import com.rubminds.api.chat.domain.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ChatRoomRepositoryCustom {

    Page<ChatRoom> findAllById(Long loginUserId, Pageable pageable);

}
