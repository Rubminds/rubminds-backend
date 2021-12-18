package com.rubminds.api.post.service;

import com.rubminds.api.post.domain.PostLike;
import com.rubminds.api.post.domain.repository.PostLikeRepository;
import com.rubminds.api.post.domain.Post;
import com.rubminds.api.post.domain.repository.PostRepository;
import com.rubminds.api.post.dto.PostLikeRequest;
import com.rubminds.api.post.dto.PostRequest;
import com.rubminds.api.post.dto.PostResponse;
import com.rubminds.api.post.exception.PostNotFoundException;
import com.rubminds.api.skill.exception.SkillNotFoundException;
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
    private final PostLikeRepository postLikeRepository;

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

    public PostResponse.Info getPost(User user, Long postId) {
        Post post = findPost(postId);
        List<PostSkill> postskills = postSkillRepository.findAllByPost(post);
        List<CostomSkill> customSkills = costomSkillRepository.findAllByPost(post);
        boolean postLike = getPostLikeStatus(user, post);
        return PostResponse.Info.build(post,postskills,customSkills,postLike);
    }

    public PostResponse.GetPosts getPosts(User user) {
        List<Post> postList = postRepository.findAll();
        return createPosts(postList, user);
    }

    public PostResponse.GetPosts getLikePosts(User user) {
        List<PostLike> postLikes = postLikeRepository.findAllByUser(user);
        List<Post> postList = new ArrayList<>();
        for (PostLike postLike : postLikes) {
            Post post = postLike.getPost();
            postList.add(post);
        }
        return createPosts(postList, user);
    }

    private PostResponse.GetPosts createPosts(List<Post> postList, User user) {
        List<PostResponse.GetPost> posts = new ArrayList<>();
        for (Post post : postList) {
            posts.add(PostResponse.GetPost.build(post, getPostLikeStatus(user, post)));
        }
        return PostResponse.GetPosts.build(posts);
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

    @Transactional
    public PostResponse.GetPostLike updatePostLike(User user, PostLikeRequest.Update request){
        Post post = findPost(request.getPostId());
        if(getPostLikeStatus(user, post)){
            postLikeRepository.deleteByUserAndPost(user, post);
            return PostResponse.GetPostLike.build(false);
        }else{
            postLikeRepository.save(PostLike.create(user, post));
            return PostResponse.GetPostLike.build(true);
        }
    }

    private Post findPost(Long postId){
        return postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
    }

    private boolean getPostLikeStatus(User user, Post post){
        return postLikeRepository.existsByUserAndPost(user, post);
    }
}
