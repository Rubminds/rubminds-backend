package com.rubminds.api.skill.service;

import com.rubminds.api.post.domain.Post;
import com.rubminds.api.post.domain.repository.PostRepository;
import com.rubminds.api.post.exception.PostNotFoundException;
import com.rubminds.api.skill.Exception.SkillNotFoundException;
import com.rubminds.api.skill.domain.PostSkill;
import com.rubminds.api.skill.domain.Skill;
import com.rubminds.api.skill.domain.UserSkill;
import com.rubminds.api.skill.domain.repository.PostSkillRepository;
import com.rubminds.api.skill.domain.repository.SkillRepository;
import com.rubminds.api.skill.dto.PostSkillRequest;
import com.rubminds.api.skill.dto.PostSkillResponse;
import com.rubminds.api.user.domain.User;
import com.rubminds.api.user.dto.AuthRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostSkillService {
    private final PostSkillRepository postSkillRepository;
    private final PostRepository postRepository;
    private final SkillRepository skillRepository;

    public PostSkillResponse.addSkill create(PostSkillRequest request) {
            Skill skill = skillRepository.findById(request.getSkill_id()).orElseThrow(PostNotFoundException::new);
            Post post = postRepository.findById(request.getPost_id()).orElseThrow(PostNotFoundException::new);

            PostSkill postSkill = PostSkill.create(skill, post);
            PostSkill savedPostskill = postSkillRepository.save(postSkill);

            return PostSkillResponse.addSkill.build(savedPostskill);
        }



    public void delete(Long postskillid) {
        postSkillRepository.deleteById(postskillid);
    }

    public List<PostSkill> findAll(Long postid) {
        Post post = postRepository.findById(postid).orElseThrow(PostNotFoundException::new);
        List<PostSkill> skills = postSkillRepository.findAllByPost(post);
        return skills;
    }

}
