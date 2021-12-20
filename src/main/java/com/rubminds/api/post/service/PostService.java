package com.rubminds.api.post.service;

import com.rubminds.api.post.domain.Post;
import com.rubminds.api.post.domain.PostLike;
import com.rubminds.api.post.domain.PostSkill;
import com.rubminds.api.post.domain.repository.PostLikeRepository;
import com.rubminds.api.post.domain.repository.PostRepository;
import com.rubminds.api.post.domain.repository.PostSkillRepository;
import com.rubminds.api.post.dto.PostLikeRequest;
import com.rubminds.api.post.dto.PostRequest;
import com.rubminds.api.post.dto.PostResponse;
import com.rubminds.api.post.exception.PostNotFoundException;
import com.rubminds.api.skill.domain.CustomSkill;
import com.rubminds.api.skill.domain.repository.CustomSkillRepository;
import com.rubminds.api.skill.domain.Skill;
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
    private final TeamUserRepository teamUserRepository;
    private final PostLikeRepository postLikeRepository;

    @Transactional
    public PostResponse.OnlyId createRecruitProjectOrStudy(PostRequest.CreateOrUpdate request, User user) {
        List<Skill> skills = skillRepository.findAllByIdIn(request.getSkillIds());
        Post post = Post.createRecruitProjectOrStudy(request, user);
        Post savedPost = createPost(request, user, skills, post);
        return PostResponse.OnlyId.build(savedPost);
    }

    @Transactional
    public PostResponse.OnlyId createRecruitScout(PostRequest.CreateOrUpdate request, User user) {
        List<Skill> skills = skillRepository.findAllByIdIn(request.getSkillIds());
        Post post = Post.createRecruitScout(request, user);
        Post savedPost = createPost(request, user, skills, post);
        return PostResponse.OnlyId.build(savedPost);
    }

    private Post createPost(PostRequest.CreateOrUpdate request, User user, List<Skill> skills, Post post) {
        List<PostSkill> postSkills = skills.stream().map(skill -> PostSkill.create(skill, post)).collect(Collectors.toList());
        List<CustomSkill> customSkills = request.getCustomSkillName().stream().map(name -> CustomSkill.create(name, post)).collect(Collectors.toList());

        Post savedPost = postRepository.save(post);
        postSkillRepository.saveAll(postSkills);
        customSkillRepository.saveAll(customSkills);

        Team team = Team.create(user, savedPost);
        teamRepository.save(team);

        TeamUser teamUser = TeamUser.create(team,user);
        teamUserRepository.save(teamUser);

        return savedPost;
    }

    public PostResponse.Info getPost(Long postId) {
        Post post = postRepository.findByIdWithCustomSkillAndUser(postId).orElseThrow(PostNotFoundException::new);
        List<Skill> skills = skillRepository.findAllByPost(postId);
        Team team = teamRepository.findByPostId(postId);
        return PostResponse.Info.build(post, skills,team);
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

}
