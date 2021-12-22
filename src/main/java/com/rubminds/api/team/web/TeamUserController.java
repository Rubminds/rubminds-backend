package com.rubminds.api.team.web;

import com.rubminds.api.team.Service.TeamUserService;
import com.rubminds.api.team.dto.TeamUserRequest;
import com.rubminds.api.team.dto.TeamUserResponse;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<TeamUserResponse.OnlyId> addTeamUser(@RequestBody TeamUserRequest.Create request) {
        TeamUserResponse.OnlyId response = teamUserService.add(request);
        return ResponseEntity.created(URI.create("/api/teamUser/" + response.getId())).body(response);
    }

    @GetMapping("/teamUsers")
    public ResponseEntity<List<TeamUserResponse.GetList>> getList(@RequestParam(name = "teamId") Long teamId){
        List<TeamUserResponse.GetList> response = teamUserService.getList(teamId);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/teamUser/evaluateProject")
    public ResponseEntity<TeamUserResponse.OnlyId> evaluate(@RequestBody TeamUserRequest.EvaluateProject request) {
        TeamUserResponse.OnlyId response = teamUserService.evaluateProject(request);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/teamUser/evaluateStudy")
    public ResponseEntity<TeamUserResponse.OnlyId> evaluate(@RequestBody TeamUserRequest.EvaluateStudy request) {
        TeamUserResponse.OnlyId response = teamUserService.evaluateStudy(request);
        return ResponseEntity.ok().body(response);
    }


    @DeleteMapping("/teamUser/{teamUserId}")
    public Long delete(@PathVariable("teamUserId") Long teamUserId) {
        return teamUserService.delete(teamUserId);

    }



}
