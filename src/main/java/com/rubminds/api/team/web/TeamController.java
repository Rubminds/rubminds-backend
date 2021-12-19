package com.rubminds.api.team.web;

import com.rubminds.api.team.Service.TeamService;
import com.rubminds.api.team.dto.TeamResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/team")
public class TeamController {
    private final TeamService teamService;


    @GetMapping("/{teamId}")
    public ResponseEntity<TeamResponse.GetTeam> TeamInfo(@PathVariable Long teamId) {
        TeamResponse.GetTeam infoResponse = teamService.getTeamInfo(teamId);
        return ResponseEntity.ok().body(infoResponse);
    }

}

