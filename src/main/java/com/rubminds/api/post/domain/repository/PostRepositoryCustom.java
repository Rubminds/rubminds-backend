package com.rubminds.api.post.domain.repository;

import com.rubminds.api.post.domain.Kinds;
import com.rubminds.api.post.domain.Post;
import com.rubminds.api.post.domain.PostStatus;
import com.rubminds.api.user.domain.User;
import com.rubminds.api.user.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PostRepositoryCustom {
    Optional<Post> findByIdWithSkillAndUser(Long postId);

    Page<Post> findAllByKindsAndStatus(Kinds kinds, PostStatus postStatus, Pageable of);

    List<UserDto.ProjectInfo> findCountByStatusAndUser(Long userId);
}
