package com.rubminds.api.team.web;

import com.rubminds.api.team.Service.TeamUserService;
import com.rubminds.api.team.dto.TeamUserRequest;
import com.rubminds.api.team.dto.TeamUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class TeamUserController {

    private final TeamUserService teamUserService;

    @PostMapping("/teamUser/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TeamUserResponse.OnlyId> addTeamUser(@RequestBody TeamUserRequest.Create request) {
        TeamUserResponse.OnlyId response = teamUserService.add(request);
        return ResponseEntity.created(URI.create("/api/teamUsers/" + request.getTeamId())).body(response);
    }

    @GetMapping("/teamUsers/{teamId}")
    public ResponseEntity<List<TeamUserResponse.GetList>> getList(@PathVariable Long teamId){
        List<TeamUserResponse.GetList> response = teamUserService.getList(teamId);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/teamUser/evaluate/{teamUserId}")
    public ResponseEntity<TeamUserResponse.OnlyId> evaluate(@PathVariable Long teamUserId, @RequestBody TeamUserRequest.Evaluate request) {
        TeamUserResponse.OnlyId response = teamUserService.evaluate(teamUserId, request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/teamUser/{teamUserId}")
    public Long delete(@PathVariable("teamUserId") Long teamUserId) {
        return teamUserService.delete(teamUserId);

    }



}
