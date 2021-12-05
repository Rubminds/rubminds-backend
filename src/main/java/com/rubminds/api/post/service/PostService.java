package com.rubminds.api.post.service;

import com.rubminds.api.post.domain.Post;
import com.rubminds.api.post.domain.repository.PostRepository;
import com.rubminds.api.post.dto.PostRequest;
import com.rubminds.api.post.dto.PostResponse;
import com.rubminds.api.post.exception.PostNotFoundException;
import com.rubminds.api.skill.Exception.SkillNotFoundException;
import com.rubminds.api.skill.domain.PostSkill;
import com.rubminds.api.skill.domain.Skill;
import com.rubminds.api.skill.domain.repository.PostSkillRepository;
import com.rubminds.api.skill.domain.repository.SkillRepository;
import com.rubminds.api.user.domain.User;
import com.rubminds.api.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostSkillRepository postSkillRepository;
    private final SkillRepository skillRepository;

    public PostResponse.OnlyId create(PostRequest.Create request, User user) {
        Post post = Post.create(request, user);
        setPostSkills(request,post);
        Post savedPost = postRepository.save(post);
        return PostResponse.OnlyId.build(savedPost);
    }

    public PostResponse.Info getPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        List<PostSkill> skills = postSkillRepository.findAllByPost(post);
        return PostResponse.Info.build(post,post.getWriter(),skills);
    }

    @Transactional
    public PostResponse.OnlyId update(Long postId, PostRequest.Create request) {
        Post post = postRepository.findById(postId).orElseThrow(UserNotFoundException::new);
        post.update(request);

        postSkillRepository.deleteAllByPost(post);
        setPostSkills(request,post);

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

    public void delete(Long postId) {
        postRepository.deleteById(postId);
    }


}
