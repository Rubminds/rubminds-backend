package com.rubminds.api.team.domain.repository;

import com.rubminds.api.post.domain.Post;
import com.rubminds.api.team.domain.Team;
import com.rubminds.api.team.domain.TeamUser;
import com.rubminds.api.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;



public interface TeamUserRepository extends JpaRepository<TeamUser, Long> {
    List<TeamUser> findAllByTeam(Team team);

    boolean existsByUserAndTeam(User user, Team team);

}
