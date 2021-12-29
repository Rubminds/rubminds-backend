package com.rubminds.api.team.web;

import com.rubminds.api.team.Service.TeamUserService;
import com.rubminds.api.team.dto.TeamUserRequest;
import com.rubminds.api.team.dto.TeamUserResponse;
import com.rubminds.api.user.security.userdetails.CurrentUser;
import com.rubminds.api.user.security.userdetails.CustomUserDetails;
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

    @PostMapping("/team/{teamId}/user/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TeamUserResponse.OnlyId> addTeamUser(@PathVariable Long teamId, @PathVariable Long userId, @CurrentUser CustomUserDetails customUserDetails) {
        TeamUserResponse.OnlyId response = teamUserService.add(teamId, userId, customUserDetails.getUser());
        return ResponseEntity.created(URI.create("/api/teamUsers/" + teamId)).body(response);
    }

    @GetMapping("/team/{teamId}/teamUsers")
    public ResponseEntity<List<TeamUserResponse.GetList>> getList(@PathVariable Long teamId){
        List<TeamUserResponse.GetList> response = teamUserService.getList(teamId);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/teamUser/{teamUserId}")
    public ResponseEntity<TeamUserResponse.OnlyId> evaluate(@PathVariable Long teamUserId, @RequestBody TeamUserRequest.Evaluate request) {
        TeamUserResponse.OnlyId response = teamUserService.evaluate(teamUserId, request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/teamUser/{teamUserId}")
    public Long delete(@PathVariable("teamUserId") Long teamUserId) {
        return teamUserService.delete(teamUserId);

    }



}
