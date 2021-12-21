package com.rubminds.api.team.Service;

import com.rubminds.api.team.domain.Team;
import com.rubminds.api.team.domain.TeamUser;
import com.rubminds.api.team.domain.repository.TeamRepository;
import com.rubminds.api.team.domain.repository.TeamUserRepository;
import com.rubminds.api.team.dto.TeamUserRequest;
import com.rubminds.api.team.dto.TeamUserResponse;
import com.rubminds.api.team.exception.DuplicateTeamUserException;
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

    public TeamUserResponse.OnlyId add(TeamUserRequest.Create request) {
        User applicant = userRepository.findById(request.getUserId()).orElseThrow(UserNotFoundException::new);
        Team team = teamRepository.findById(request.getTeamId()).orElseThrow(TeamNotFoundException::new);
        if(teamUserRepository.existsByUserAndTeam(applicant, team)){
            throw new DuplicateTeamUserException();
        }
        TeamUser teamUser = TeamUser.create(applicant, team);
        teamUserRepository.save(teamUser);
        return TeamUserResponse.OnlyId.build(teamUser);
    }

    public TeamUserResponse.OnlyId evaluate(TeamUserRequest.Evaluate request) {
        TeamUser evaluator = teamUserRepository.findById(request.getTeamUserId()).orElseThrow(TeamUserNotFoundException::new);
        for(int i = 0 ; i<request.getEvaluation().size(); i++){
            TeamUser target = teamUserRepository.findById(request.getEvaluation().get(i).getTeamUserId()).orElseThrow(TeamUserNotFoundException::new);
            double attendLevel = request.getEvaluation().get(i).getAttendLevel();
            double workLevel = request.getEvaluation().get(i).getWorkLevel();
            target.updateLevel(attendLevel, workLevel);
        }
        evaluator.updateFinish();
        return TeamUserResponse.OnlyId.build(evaluator);
    }

    public Long delete(Long teamUserId) {
        teamUserRepository.deleteById(teamUserId);
        return teamUserId;
    }


}
