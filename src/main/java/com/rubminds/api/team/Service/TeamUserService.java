package com.rubminds.api.team.Service;
import com.rubminds.api.team.domain.Team;
import com.rubminds.api.team.domain.TeamUser;
import com.rubminds.api.team.domain.repository.TeamRepository;
import com.rubminds.api.team.domain.repository.TeamUserRepository;
import com.rubminds.api.team.dto.TeamUserRequest;
import com.rubminds.api.team.dto.TeamUserResponse;
import com.rubminds.api.team.exception.TeamNotFoundException;
import com.rubminds.api.team.exception.TeamUserNotFoundException;
import com.rubminds.api.user.domain.User;
import com.rubminds.api.user.domain.repository.UserRepository;
import com.rubminds.api.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class TeamUserService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final TeamUserRepository teamUserRepository;

    public TeamUserResponse.OnlyId create(Long userid, TeamUserRequest.Create request) {
        User user = userRepository.findById(userid).orElseThrow(UserNotFoundException::new);
        Team team = teamRepository.findById(request.getTeam_id()).orElseThrow(TeamNotFoundException::new);
        TeamUser teamUser = TeamUser.create(team,user);

        teamUserRepository.save(teamUser);
        return TeamUserResponse.OnlyId.build(teamUser);
    }

    public TeamUserResponse.OnlyId update(Long teamUserId) {
        TeamUser teamUser = teamUserRepository.findById(teamUserId).orElseThrow(TeamUserNotFoundException::new);
        teamUser.update(teamUser);
        return TeamUserResponse.OnlyId.build(teamUser);
    }


    public Long delete(Long teamUserId) {
        teamUserRepository.deleteById(teamUserId);
        return teamUserId;
    }


}
