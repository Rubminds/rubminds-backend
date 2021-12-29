package com.rubminds.api.team.Service;

import com.rubminds.api.post.domain.Kinds;
import com.rubminds.api.team.domain.Team;
import com.rubminds.api.team.domain.TeamUser;
import com.rubminds.api.team.domain.repository.TeamRepository;
import com.rubminds.api.team.domain.repository.TeamUserRepository;
import com.rubminds.api.team.dto.TeamUserRequest;
import com.rubminds.api.team.dto.TeamUserResponse;
import com.rubminds.api.team.exception.DuplicateTeamUserException;
import com.rubminds.api.team.exception.EvaluateException;
import com.rubminds.api.team.exception.TeamNotFoundException;
import com.rubminds.api.team.exception.TeamUserNotFoundException;
import com.rubminds.api.user.domain.User;
import com.rubminds.api.user.domain.repository.UserRepository;
import com.rubminds.api.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
        TeamUser teamUser = TeamUser.createWithTeam(applicant, team);
        teamUserRepository.save(teamUser);
        return TeamUserResponse.OnlyId.build(teamUser);
    }

    public List<TeamUserResponse.GetList> getList(Long teamId){
        Team team = teamRepository.findById(teamId).orElseThrow(TeamNotFoundException::new);
        List<TeamUser> teamUsers = teamUserRepository.findAllByTeam(team);
        return teamUsers.stream().map(TeamUserResponse.GetList::build).collect(Collectors.toList());
    }

    public TeamUserResponse.OnlyId evaluate(Long teamUserId, TeamUserRequest.Evaluate request) {
        TeamUser evaluator = teamUserRepository.findById(teamUserId).orElseThrow(TeamUserNotFoundException::new);
        if(evaluator.isFinish()){
            throw new EvaluateException();
        }
        evaluateTeam(request);
        evaluator.updateFinish();
        return TeamUserResponse.OnlyId.build(evaluator);
    }

    public Long delete(Long teamUserId) {
        TeamUser teamUser = teamUserRepository.findById(teamUserId).orElseThrow(TeamUserNotFoundException::new);
        teamUser.getUser().updateAttendLevel(0);
        teamUserRepository.deleteById(teamUserId);
        return teamUserId;
    }

    private void evaluateTeam(TeamUserRequest.Evaluate request){
        for(int i = 0 ; i<request.getEvaluation().size(); i++){
            TeamUser target = teamUserRepository.findById(request.getEvaluation().get(i).getTeamUserId()).orElseThrow(TeamUserNotFoundException::new);
            User user = target.getUser();
            user.updateAttendLevel(request.getEvaluation().get(i).getAttendLevel());
            if(request.getKinds() == Kinds.PROJECT){
                user.updateWorkLevel(request.getEvaluation().get(i).getWorkLevel());
            }
        }
    }
}
