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
        TeamUser teamUser = TeamUser.create(applicant, team);
        teamUserRepository.save(teamUser);
        return TeamUserResponse.OnlyId.build(teamUser);
    }

    public List<TeamUserResponse.GetList> getList(Long teamId){
        Team team = teamRepository.findById(teamId).orElseThrow(TeamNotFoundException::new);
        List<TeamUser> teamUsers = teamUserRepository.findAllByTeam(team);
        return teamUsers.stream().map(TeamUserResponse.GetList::build).collect(Collectors.toList());
    }

    public TeamUserResponse.OnlyId evaluateProject(TeamUserRequest.EvaluateProject request) {
        TeamUser evaluator = teamUserRepository.findById(request.getTeamUserId()).orElseThrow(TeamUserNotFoundException::new);
        for(int i = 0 ; i<request.getEvaluation().size(); i++){
            TeamUser target = teamUserRepository.findById(request.getEvaluation().get(i).getTeamUserId()).orElseThrow(TeamUserNotFoundException::new);
            double attendLevel = target.getAttendLevel() + request.getEvaluation().get(i).getAttendLevel();
            double workLevel = target.getWorkLevel() + request.getEvaluation().get(i).getWorkLevel();
            target.updateLevel(attendLevel, workLevel);
        }
        evaluator.updateFinish();
        isFinishProject(evaluator.getTeam().getId());
        return TeamUserResponse.OnlyId.build(evaluator);
    }

    public TeamUserResponse.OnlyId evaluateStudy(TeamUserRequest.EvaluateStudy request) {
        TeamUser evaluator = teamUserRepository.findById(request.getTeamUserId()).orElseThrow(TeamUserNotFoundException::new);
        for(int i = 0 ; i<request.getEvaluation().size(); i++){
            TeamUser target = teamUserRepository.findById(request.getEvaluation().get(i).getTeamUserId()).orElseThrow(TeamUserNotFoundException::new);
            double attendLevel = target.getAttendLevel() + request.getEvaluation().get(i).getAttendLevel();
            target.updateAttendLevel(attendLevel);
        }
        evaluator.updateFinish();
        isFinishStudy(evaluator.getTeam().getId());
        return TeamUserResponse.OnlyId.build(evaluator);
    }

    public Long delete(Long teamUserId) {
        TeamUser teamUser = teamUserRepository.findById(teamUserId).orElseThrow(TeamUserNotFoundException::new);
        User user = userRepository.findById(teamUser.getUser().getId()).orElseThrow(UserNotFoundException::new);
        user.updateAttendLevel(user.getAttendLevel()-1);
        teamUserRepository.deleteById(teamUserId);
        return teamUserId;
    }

    // 아래 모두 테스트용 (완료게시글 작성 완료 시 실행)
    private void isFinishProject(Long teamId){
        Team team = teamRepository.findById(teamId).orElseThrow(TeamNotFoundException::new);
        Integer cntFinish = teamUserRepository.countAllByTeamAndFinishIsTrue(team);
        if(team.getTeamUsers().size()==cntFinish){
            for(int i = 0; i<team.getTeamUsers().size();i++){
                User user = userRepository.findById(team.getTeamUsers().get(i).getUser().getId()).orElseThrow(UserNotFoundException::new);
                double attendLevel = team.getTeamUsers().get(i).getAttendLevel()/(team.getTeamUsers().size()-1);
                double workLevel = team.getTeamUsers().get(i).getWorkLevel()/(team.getTeamUsers().size()-1);
                user.updateAttendLevel(calcAttendLevel(user,attendLevel));
                user.updateWorkLevel(calcWorkLevel(user,workLevel));
            }
        }
    }

    private void isFinishStudy(Long teamId){
        Team team = teamRepository.findById(teamId).orElseThrow(TeamNotFoundException::new);
        Integer cntFinish = teamUserRepository.countAllByTeamAndFinishIsTrue(team);
        if(team.getTeamUsers().size()==cntFinish){
            for(int i = 0; i<team.getTeamUsers().size();i++){
                User user = userRepository.findById(team.getTeamUsers().get(i).getUser().getId()).orElseThrow(UserNotFoundException::new);
                double attendLevel = team.getTeamUsers().get(i).getAttendLevel()/(team.getTeamUsers().size()-1);
                user.updateAttendLevel(calcAttendLevel(user, attendLevel));
            }
        }
    }

    private double calcAttendLevel(User user, double attendLevel){
        if(user.getAttendLevel()!=0) {
            attendLevel = (user.getAttendLevel() + attendLevel) / 2;
        }
        return attendLevel;
    }

    private double calcWorkLevel(User user, double workLevel){
        if(user.getWorkLevel()!=0){
            workLevel = (user.getWorkLevel() + workLevel)/2;
        }
        return workLevel;
    }
}
