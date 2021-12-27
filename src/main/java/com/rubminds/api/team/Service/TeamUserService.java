package com.rubminds.api.team.Service;
import com.rubminds.api.common.exception.PermissionException;
import com.rubminds.api.team.domain.Team;
import com.rubminds.api.team.domain.TeamUser;
import com.rubminds.api.team.domain.repository.TeamRepository;
import com.rubminds.api.team.domain.repository.TeamUserRepository;
import com.rubminds.api.team.dto.TeamUserRequest;
import com.rubminds.api.team.dto.TeamUserResponse;
import com.rubminds.api.team.exception.AlreadyExitException;
import com.rubminds.api.team.exception.TeamNotFoundException;
import com.rubminds.api.team.exception.TeamUserNotFoundException;
import com.rubminds.api.user.domain.User;
import com.rubminds.api.user.domain.repository.UserRepository;
import com.rubminds.api.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Permission;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class TeamUserService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamUserRepository teamUserRepository;

    public TeamUserResponse.OnlyId addTeamUser(Long userId, TeamUserRequest.CreateAndDelete request, User currentUser) {

        Team team = teamRepository.findById(request.getTeam_id()).orElseThrow(TeamNotFoundException::new);
        PermissionCheck(currentUser, team.getAdmin());

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        AlreadyExistsCheck(user,team);
        TeamUser teamUser = TeamUser.create(team,user);
        teamUserRepository.save(teamUser);

        return TeamUserResponse.OnlyId.build(teamUser);
    }


    public TeamUserResponse.OnlyId changeFinish(Long teamUserId, User member) {
        TeamUser teamUser = teamUserRepository.findById(teamUserId).orElseThrow(TeamUserNotFoundException::new);
        PermissionCheck(member, teamUser.getUser());

        teamUser.update(teamUser);
        return TeamUserResponse.OnlyId.build(teamUser);
    }


    public Long deleteUser(Long teamUserId, User currentUser, Long teamId) {
        User admin = userRepository.findByTeamId(teamId).orElseThrow(UserNotFoundException::new);
        PermissionCheck(currentUser,admin);

        teamUserRepository.deleteById(teamUserId);
        return teamUserId;
    }

    private void PermissionCheck(User currentUser, User admin) {
        if (currentUser.getId() != admin.getId()) {
            throw new PermissionException();
        }
    }

    private void AlreadyExistsCheck(User user, Team team) {
        boolean check = teamUserRepository.existsByUserAndTeam(user,team);
        if (check == true) {
            throw new AlreadyExitException();
        }
    }

}
