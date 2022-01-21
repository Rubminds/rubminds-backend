package com.rubminds.api.chat.domain.repository;

import com.rubminds.api.chat.dto.ChatDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ChatRepositoryCustom {
    Page<ChatDto.GetChat> findAllByPostId(Long chatRoomId, Pageable pageable);

}
