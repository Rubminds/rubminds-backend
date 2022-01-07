package com.rubminds.api.team.web;

import com.rubminds.api.team.service.TeamUserService;
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
    public ResponseEntity<TeamUserResponse.OnlyId> addTeamUser(@PathVariable(value = "teamId") Long teamId, @PathVariable(value = "userId") Long userId, @CurrentUser CustomUserDetails customUserDetails) {
        TeamUserResponse.OnlyId response = teamUserService.add(teamId, userId, customUserDetails.getUser());
        return ResponseEntity.created(URI.create("/api/teamUsers/" + teamId)).body(response);
    }

    @GetMapping("/team/{teamId}/teamUsers")
    public ResponseEntity<List<TeamUserResponse.GetTeamUser>> getList(@PathVariable Long teamId){
        List<TeamUserResponse.GetTeamUser> response = teamUserService.getList(teamId);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/team/{teamId}")
    public ResponseEntity<TeamUserResponse.OnlyId> evaluate(@PathVariable Long teamId, @RequestBody TeamUserRequest.Evaluate request, @CurrentUser CustomUserDetails customUserDetails) {
        TeamUserResponse.OnlyId response = teamUserService.evaluate(teamId, request, customUserDetails.getUser());
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/team/{teamId}/user/{userId}")
    public Long delete(@PathVariable(value = "teamId") Long teamId, @PathVariable(value = "userId") Long userId, @CurrentUser CustomUserDetails customUserDetails) {
        return teamUserService.delete(teamId, userId, customUserDetails.getUser());
    }
}
