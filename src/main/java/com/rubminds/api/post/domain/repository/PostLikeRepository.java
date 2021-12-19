package com.rubminds.api.post.domain.repository;

import com.rubminds.api.post.domain.PostLike;
import com.rubminds.api.post.domain.Post;
import com.rubminds.api.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByUserAndPost(User user, Post post);
    void deleteByUserAndPost(User user, Post post);
    List<PostLike> findAllByUser(User user);
}
