package com.rubminds.api.team.web;

import com.rubminds.api.team.Service.TeamService;
import com.rubminds.api.team.Service.TeamUserService;
import com.rubminds.api.team.dto.TeamUserRequest;
import com.rubminds.api.team.dto.TeamUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/team-user")
public class TeamUserController {

    private final TeamUserService teamUserService;

    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TeamUserResponse.OnlyId> saveTeamUser(@PathVariable Long userId, @RequestBody TeamUserRequest.Create request) {
        TeamUserResponse.OnlyId response = teamUserService.create(userId,request);

        return ResponseEntity.created(URI.create("/api/team-user/" + response.getId())).body(response);
    }

    @PutMapping("/{teamUserId}")
    public ResponseEntity<TeamUserResponse.OnlyId> changeFinish(@PathVariable Long teamUserId) {
        TeamUserResponse.OnlyId response = teamUserService.update(teamUserId);
        return ResponseEntity.ok().body(response);
    }


    @DeleteMapping("/{teamUserId}")
    public Long delete(@PathVariable("teamUserId") Long teamUserId) {
        return teamUserService.delete(teamUserId);

    }
}
