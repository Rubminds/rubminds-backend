package com.rubminds.api.team.web;

import com.rubminds.api.post.dto.PostResponse;
import com.rubminds.api.skill.dto.SkillResponse;
import com.rubminds.api.team.Service.TeamService;
import com.rubminds.api.team.domain.Team;
import com.rubminds.api.team.dto.TeamResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/team")
public class TeamController {
    private final TeamService teamService;

    @GetMapping("/{postId}")
    public ResponseEntity<TeamResponse.GetTeam> TeamInfo(@PathVariable Long postId) {
        TeamResponse.GetTeam infoResponse = teamService.getTeamInfo(postId);
        return ResponseEntity.ok().body(infoResponse);
    }
}

