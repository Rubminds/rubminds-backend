package com.rubminds.api.chat.domain.repository;

import com.rubminds.api.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomRepositoryCustom{

}

