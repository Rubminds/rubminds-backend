package com.rubminds.api.team.domain.repository;

import com.rubminds.api.post.domain.Post;
import com.rubminds.api.skill.domain.Skill;
import com.rubminds.api.team.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query("select t from Team t join t.post p where p.id=:postId")
    Team findByPostId(Long postId);

    Optional<Team> deleteAllByPost(Post post);


}
