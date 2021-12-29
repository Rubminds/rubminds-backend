package com.rubminds.api.post.domain.repository;

import com.rubminds.api.post.domain.Post;
import com.rubminds.api.team.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    @Query("select p from Post p left join fetch p.customSkills join fetch p.writer where p.id=:postId")
    Optional<Post> findByIdWithCustomSkillAndUser(Long postId);

    Optional<Post> findByTeam(Team team);
}

