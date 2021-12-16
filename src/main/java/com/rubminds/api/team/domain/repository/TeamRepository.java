package com.rubminds.api.team.domain.repository;

import com.rubminds.api.team.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> deleteAllByPostId(Long postid);
    Optional<Team> findByPostId(Long postid);

}
