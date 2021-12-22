package com.rubminds.api.team.domain.repository;

import com.rubminds.api.team.domain.Team;
import com.rubminds.api.team.domain.TeamUser;
import com.rubminds.api.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TeamUserRepository extends JpaRepository<TeamUser, Long> {
    List<TeamUser> findAllByTeam(Team team);

    boolean existsByUserAndTeam(User user, Team team);

    Integer countAllByTeamAndFinishIsTrue(Team team);
}
