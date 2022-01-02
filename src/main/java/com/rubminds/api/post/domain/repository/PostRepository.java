package com.rubminds.api.post.domain.repository;

import com.rubminds.api.post.domain.Post;
import com.rubminds.api.team.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    Optional<Post> findByTeam(Team team);
}

