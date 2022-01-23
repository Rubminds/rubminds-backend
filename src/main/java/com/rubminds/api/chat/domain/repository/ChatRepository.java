package com.rubminds.api.chat.domain.repository;

import com.rubminds.api.chat.domain.Chat;
import com.rubminds.api.chat.dto.ChatDto;
import com.rubminds.api.post.domain.Post;
import com.rubminds.api.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ChatRepository extends JpaRepository<Chat, Long>, ChatRepositoryCustom {
    Page<Chat> findAllByPostId(Long postId, Pageable pageable);

    boolean existsBySenderAndPost(User loginUser, Post post);

}

