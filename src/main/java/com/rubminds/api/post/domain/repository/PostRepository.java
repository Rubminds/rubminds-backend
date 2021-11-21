package com.rubminds.api.post.domain.repository;

import com.rubminds.api.post.domain.Post;
import com.rubminds.api.post.domain.PostSearch;
import com.rubminds.api.post.dto.PostResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
   // Optional<Post> findByStatus(String status);

    Optional<Post> findByRegion(String region);


    List<PostResponse.Info> findAllByString(PostSearch postSearch);
}

