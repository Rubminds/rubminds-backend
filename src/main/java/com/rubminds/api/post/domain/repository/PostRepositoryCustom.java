package com.rubminds.api.post.domain.repository;

import com.rubminds.api.post.domain.Kinds;
import com.rubminds.api.post.domain.Post;
import com.rubminds.api.post.domain.PostStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PostRepositoryCustom {
    Optional<Post> findByIdWithSkillAndUser(Long postId);

    Page<Post> findAllByKindsAndStatus(Kinds kinds, PostStatus postStatus, Pageable of);
}
