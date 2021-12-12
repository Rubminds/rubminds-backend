package com.rubminds.api.team.Service;

import com.rubminds.api.post.domain.Post;
import com.rubminds.api.post.domain.repository.PostRepository;
import com.rubminds.api.post.exception.PostNotFoundException;
import com.rubminds.api.skill.domain.Skill;
import com.rubminds.api.skill.dto.SkillResponse;
import com.rubminds.api.team.domain.Team;
import com.rubminds.api.team.domain.repository.TeamRepository;
import com.rubminds.api.team.dto.TeamResponse;
import com.rubminds.api.team.exception.TeamNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamService {
    private final TeamRepository teamRepository;
    private final PostRepository postRepository;

    public TeamResponse.GetTeam getTeamInfo(Long postid){
        Post post = postRepository.findById(postid).orElseThrow(PostNotFoundException::new);;
        Team team = teamRepository.findByPost(post).orElseThrow(TeamNotFoundException::new);
        return TeamResponse.GetTeam.build(team);
    }
}
