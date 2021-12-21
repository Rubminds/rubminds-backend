package com.rubminds.api.post.domain.repository;

import com.rubminds.api.post.domain.Post;

import java.util.Optional;

public interface PostRepositoryCustom {
    Optional<Post> findByIdWithSkillAndUser(Long postId);
}
