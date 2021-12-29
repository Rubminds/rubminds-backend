package com.rubminds.api.team.Service;

import com.rubminds.api.post.domain.Kinds;
import com.rubminds.api.post.domain.Post;
import com.rubminds.api.post.domain.repository.PostRepository;
import com.rubminds.api.post.exception.PostNotFoundException;
import com.rubminds.api.team.domain.Team;
import com.rubminds.api.team.domain.TeamUser;
import com.rubminds.api.team.domain.repository.TeamRepository;
import com.rubminds.api.team.domain.repository.TeamUserRepository;
import com.rubminds.api.team.dto.TeamUserRequest;
import com.rubminds.api.team.dto.TeamUserResponse;
import com.rubminds.api.team.exception.*;
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
    private final PostRepository postRepository;

    public TeamUserResponse.OnlyId add(Long teamId, Long userId, User user) {
        Team team = teamRepository.findById(teamId).orElseThrow(TeamNotFoundException::new);
        if(!team.getAdmin().getId().equals(user.getId())){
            throw new NotAdminException();
        }
        Post post = postRepository.findByTeam(team).orElseThrow(PostNotFoundException::new);
        if(post.getHeadcount() == teamUserRepository.countAllByTeam(team)){
            throw new TeamFullException();
        }
        User applicant = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
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

    public TeamUserResponse.OnlyId evaluate(Long teamUserId, TeamUserRequest.Evaluate request) {
        TeamUser evaluator = teamUserRepository.findById(teamUserId).orElseThrow(TeamUserNotFoundException::new);
        if(evaluator.isFinish()){
            throw new EvaluateException();
        }
        for(int i = 0 ; i<request.getEvaluation().size(); i++){
            User user = userRepository.findById(request.getEvaluation().get(i).getUserId()).orElseThrow(UserNotFoundException::new);
            user.updateAttendLevel(request.getEvaluation().get(i).getAttendLevel());
            if(request.getKinds() == Kinds.PROJECT){
                user.updateWorkLevel(request.getEvaluation().get(i).getWorkLevel());
            }
        }
        evaluator.updateFinish();
        return TeamUserResponse.OnlyId.build(evaluator);
    }

    public Long delete(Long teamUserId) {
        TeamUser teamUser = teamUserRepository.findById(teamUserId).orElseThrow(TeamUserNotFoundException::new);
        teamUser.getUser().updateAttendLevel(0);
        teamUserRepository.deleteById(teamUserId);
        return teamUserId;
    }

}
