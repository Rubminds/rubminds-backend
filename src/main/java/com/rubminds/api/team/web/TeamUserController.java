package com.rubminds.api.team.web;

import com.rubminds.api.common.exception.PermissionException;
import com.rubminds.api.team.Service.TeamService;
import com.rubminds.api.team.Service.TeamUserService;
import com.rubminds.api.team.dto.TeamResponse;
import com.rubminds.api.team.dto.TeamUserRequest;
import com.rubminds.api.team.dto.TeamUserResponse;
import com.rubminds.api.user.security.userdetails.CurrentUser;
import com.rubminds.api.user.security.userdetails.CustomUserDetails;
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
    private final TeamService teamService;

    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TeamUserResponse.OnlyId> saveTeamUser(@CurrentUser CustomUserDetails customUserDetails, @PathVariable Long userId, @RequestBody TeamUserRequest.CreateAndDelete request){
        TeamUserResponse.OnlyId response = teamUserService.addTeamUser(userId,request,customUserDetails.getUser());
        return ResponseEntity.created(URI.create("/api/team-user/" + response.getId())).body(response);
    }

    @PutMapping("/{teamUserId}")
    public ResponseEntity<TeamUserResponse.OnlyId> changeFinish(@PathVariable Long teamUserId,@CurrentUser CustomUserDetails customUserDetails) {
        TeamUserResponse.OnlyId response = teamUserService.changeFinish(teamUserId,customUserDetails.getUser());
        return ResponseEntity.ok().body(response);
    }


    @DeleteMapping("/{teamUserId}")
    public Long teamUserDelete(@PathVariable("teamUserId") Long teamUserId,@RequestBody  TeamUserRequest.CreateAndDelete request ,@CurrentUser CustomUserDetails customUserDetails) {
        return teamUserService.deleteUser(teamUserId,customUserDetails.getUser(),request.getTeam_id());

    }
}
