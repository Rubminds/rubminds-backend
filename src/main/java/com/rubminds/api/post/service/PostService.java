package com.rubminds.api.post.service;

import com.rubminds.api.post.domain.Post;
import com.rubminds.api.post.domain.repository.PostRepository;
import com.rubminds.api.post.dto.PostRequest;
import com.rubminds.api.post.dto.PostResponse;
import com.rubminds.api.post.exception.PostNotFoundException;
import com.rubminds.api.skill.Exception.SkillNotFoundException;
import com.rubminds.api.skill.domain.CostomSkill;
import com.rubminds.api.skill.domain.PostSkill;
import com.rubminds.api.skill.domain.Skill;
import com.rubminds.api.skill.domain.repository.CostomSkillRepository;
import com.rubminds.api.skill.domain.repository.PostSkillRepository;
import com.rubminds.api.skill.domain.repository.SkillRepository;
import com.rubminds.api.team.domain.Team;
import com.rubminds.api.team.domain.TeamUser;
import com.rubminds.api.team.domain.repository.TeamRepository;
import com.rubminds.api.team.domain.repository.TeamUserRepository;
import com.rubminds.api.team.exception.TeamNotFoundException;
import com.rubminds.api.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostSkillRepository postSkillRepository;
    private final SkillRepository skillRepository;
    private final CostomSkillRepository costomSkillRepository;
    private final TeamRepository teamRepository;
    private final TeamUserRepository teamUserRepository;

    @Transactional
    public PostResponse.OnlyId create(PostRequest.Create request, User user) {
        Post post = Post.create(request, user);

        setPostSkills(request,post);
        setCostomSkills(request,post);
        Post savedPost = postRepository.save(post);

        Team team = Team.create(user,savedPost);
        teamRepository.save(team);

        TeamUser teamUser = TeamUser.create(team,user);
        teamUserRepository.save(teamUser);

        return PostResponse.OnlyId.build(savedPost);
    }

    public PostResponse.Info getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        List<PostSkill> postskills = postSkillRepository.findAllByPost(post);
        List<CostomSkill> costomSkills = costomSkillRepository.findAllByPost(post);
        return PostResponse.Info.build(post,postskills,costomSkills);
    }

    @Transactional
    public PostResponse.OnlyId update(Long postId, PostRequest.Create request) {

        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        post.update(request);

        postSkillRepository.deleteAllByPost(post);
        costomSkillRepository.deleteAllByPost(post);
        setPostSkills(request,post);
        setCostomSkills(request,post);

        return PostResponse.OnlyId.build(post);
    }

    public List<PostSkill> setPostSkills(PostRequest.Create request, Post post){
        List<PostSkill> postSkills = new ArrayList<>();
        for(Long skillId : request.getPostSkillIds()){
            Skill findSkill = skillRepository.findById(skillId).orElseThrow(SkillNotFoundException::new);
            PostSkill postSkill = PostSkill.create(findSkill,post);
            postSkills.add(postSkill);
        }
        return postSkills;
    }

    public List<CostomSkill> setCostomSkills(PostRequest.Create request, Post post){
        List<CostomSkill> costomSkills = new ArrayList<>();
        for(String skill : request.getCostomSkills()){
            CostomSkill costomSkill = CostomSkill.create(skill,post);
            costomSkills.add(costomSkill);
        }
        return costomSkills;
    }

    @Transactional
    public Long delete(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);

        teamRepository.deleteAllByPost(post).orElseThrow(TeamNotFoundException::new);
        postRepository.deleteById(postId);

        return postId;
    }


}
