package com.rubminds.api.post.service;

import com.rubminds.api.post.domain.Post;
import com.rubminds.api.post.domain.PostSkill;
import com.rubminds.api.post.domain.repository.PostRepository;
import com.rubminds.api.post.dto.PostRequest;
import com.rubminds.api.post.dto.PostResponse;
import com.rubminds.api.post.exception.PostNotFoundException;
import com.rubminds.api.skill.domain.CustomSkill;
import com.rubminds.api.skill.domain.Skill;
import com.rubminds.api.skill.domain.repository.CustomSkillRepository;
import com.rubminds.api.post.domain.repository.PostSkillRepository;
import com.rubminds.api.skill.domain.repository.SkillRepository;
import com.rubminds.api.team.domain.Team;
import com.rubminds.api.team.domain.repository.TeamRepository;
import com.rubminds.api.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostSkillRepository postSkillRepository;
    private final SkillRepository skillRepository;
    private final CustomSkillRepository customSkillRepository;
    private final TeamRepository teamRepository;

    @Transactional
    public PostResponse.OnlyId createRecruit(PostRequest.CreateOrUpdate request, User user) {
        List<Skill> skills = skillRepository.findAllByIdIn(request.getSkillIds());

        Post post = Post.createRecruit(request, user);

        List<PostSkill> postSkills = skills.stream().map(skill -> PostSkill.create(skill, post)).collect(Collectors.toList());
        List<CustomSkill> customSkills = request.getCustomSkillName().stream().map(name -> CustomSkill.create(name, post)).collect(Collectors.toList());

        Post savedPost = postRepository.save(post);
        postSkillRepository.saveAll(postSkills);
        customSkillRepository.saveAll(customSkills);

        Team team = Team.create(user, savedPost);
        teamRepository.save(team);

        return PostResponse.OnlyId.build(savedPost);
    }

    public PostResponse.Info getPost(Long postId) {
        Post post = postRepository.findByIdWithCustomSkillAndUser(postId).orElseThrow(PostNotFoundException::new);
        List<Skill> skills = skillRepository.findAllByPost(postId);
        return PostResponse.Info.build(post, skills);
    }


    @Transactional
    public PostResponse.OnlyId update(Long postId, PostRequest.CreateOrUpdate request) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        post.update(request);

        postSkillRepository.deleteAllByPost(post);
        customSkillRepository.deleteAllByPost(post);

        List<Skill> skills = skillRepository.findAllByIdIn(request.getSkillIds());
        List<PostSkill> postSkills = skills.stream().map(skill -> PostSkill.create(skill, post)).collect(Collectors.toList());
        List<CustomSkill> customSkills = request.getCustomSkillName().stream().map(name -> CustomSkill.create(name, post)).collect(Collectors.toList());

        postSkillRepository.saveAll(postSkills);
        customSkillRepository.saveAll(customSkills);

        return PostResponse.OnlyId.build(post);
    }


    @Transactional
    public Long delete(Long postId) {
        postRepository.deleteById(postId);
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        teamRepository.deleteAllByPost(post);
        return postId;
    }
}
