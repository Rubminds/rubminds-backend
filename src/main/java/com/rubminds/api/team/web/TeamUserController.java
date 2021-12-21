package com.rubminds.api.team.web;

import com.rubminds.api.team.Service.TeamUserService;
import com.rubminds.api.team.dto.TeamUserRequest;
import com.rubminds.api.team.dto.TeamUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/teamUser")
public class TeamUserController {

    private final TeamUserService teamUserService;

    @PostMapping("/add")
    public ResponseEntity<TeamUserResponse.OnlyId> addTeamUser(@RequestBody TeamUserRequest.Create request) {
        TeamUserResponse.OnlyId response = teamUserService.add(request);

        return ResponseEntity.created(URI.create("/api/teamUser/" + response.getId())).body(response);
    }

    @PostMapping("/evaluate")
    public ResponseEntity<TeamUserResponse.OnlyId> evaluate(@RequestBody TeamUserRequest.Evaluate request) {
        TeamUserResponse.OnlyId response = teamUserService.evaluate(request);
        return ResponseEntity.ok().body(response);
    }


    @DeleteMapping("/{teamUserId}")
    public Long delete(@PathVariable("teamUserId") Long teamUserId) {
        return teamUserService.delete(teamUserId);

    }



}
