package com.rubminds.api.team.service;

import com.rubminds.api.team.domain.Team;
import com.rubminds.api.team.domain.TeamUser;
import com.rubminds.api.team.domain.repository.TeamRepository;
import com.rubminds.api.team.domain.repository.TeamUserRepository;
import com.rubminds.api.team.dto.TeamResponse;
import com.rubminds.api.team.exception.TeamNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final TeamUserRepository teamUserRepository;


    public TeamResponse.GetTeam getTeamInfo(Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(TeamNotFoundException::new);
        List<TeamUser> teamUsers = teamUserRepository.findAllByTeam(team);
        return TeamResponse.GetTeam.build(team, teamUsers);
    }
}

