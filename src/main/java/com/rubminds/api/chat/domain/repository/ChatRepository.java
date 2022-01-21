package com.rubminds.api.chat.domain.repository;

import com.rubminds.api.chat.domain.Chat;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ChatRepository extends JpaRepository<Chat, Long>, ChatRepositoryCustom {

}

