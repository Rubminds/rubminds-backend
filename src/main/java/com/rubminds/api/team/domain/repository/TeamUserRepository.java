package com.rubminds.api.team.domain.repository;

import com.rubminds.api.post.domain.Post;
import com.rubminds.api.team.domain.Team;
import com.rubminds.api.team.domain.TeamUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;



public interface TeamUserRepository extends JpaRepository<TeamUser, Long> {
    List<TeamUser> findAllByTeam(Team team);

}
