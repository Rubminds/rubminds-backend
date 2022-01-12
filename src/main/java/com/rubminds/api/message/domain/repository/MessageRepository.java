package com.rubminds.api.message.domain.repository;

import com.rubminds.api.message.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MessageRepository extends JpaRepository<Message, Long> {

}

