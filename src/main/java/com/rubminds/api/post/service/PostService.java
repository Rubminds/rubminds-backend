package com.rubminds.api.post.service;

import com.rubminds.api.file.service.S3Service;
import com.rubminds.api.post.domain.Post;
import com.rubminds.api.post.domain.PostFile;
import com.rubminds.api.post.domain.PostLike;
import com.rubminds.api.post.domain.PostSkill;
import com.rubminds.api.post.domain.repository.PostFileRepository;
import com.rubminds.api.post.domain.repository.PostLikeRepository;
import com.rubminds.api.post.domain.repository.PostRepository;
import com.rubminds.api.post.domain.repository.PostSkillRepository;
import com.rubminds.api.post.dto.PostRequest;
import com.rubminds.api.post.dto.PostResponse;
import com.rubminds.api.post.exception.PostNotFoundException;
import com.rubminds.api.skill.domain.CustomSkill;
import com.rubminds.api.skill.domain.Skill;
import com.rubminds.api.skill.domain.repository.CustomSkillRepository;
import com.rubminds.api.skill.domain.repository.SkillRepository;
import com.rubminds.api.team.domain.Team;
import com.rubminds.api.team.domain.repository.TeamRepository;
import com.rubminds.api.user.domain.User;
import com.rubminds.api.user.security.userdetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final PostLikeRepository postLikeRepository;
    private final S3Service s3Service;
    private final PostFileRepository postFileRepository;

    @Transactional
    public PostResponse.OnlyId create(PostRequest.CreateOrUpdate request, List<MultipartFile> files, User user) {
        Team team = Team.create(user);
        teamRepository.save(team);

        Post post = Post.create(request, team, user);
        Post savedPost = postRepository.save(post);

        List<Skill> skills = skillRepository.findAllByIdIn(request.getSkillIds());
        List<PostSkill> postSkills = skills.stream().map(skill -> PostSkill.create(skill, post)).collect(Collectors.toList());
        List<CustomSkill> customSkills = request.getCustomSkillName().stream().map(name -> CustomSkill.create(name, post)).collect(Collectors.toList());

        postSkillRepository.saveAll(postSkills);
        customSkillRepository.saveAll(customSkills);

        if (files != null) {
            List<PostFile> postFiles = s3Service.uploadFileList(files).stream().map(savedFile -> PostFile.create(savedPost, savedFile)).collect(Collectors.toList());
            postFileRepository.saveAll(postFiles);
        }

        return PostResponse.OnlyId.build(savedPost);
    }

    public PostResponse.Info getOne(Long postId, CustomUserDetails customUserDetails) {
        Post post = postRepository.findByIdWithSkillAndUser(postId).orElseThrow(PostNotFoundException::new);
        return PostResponse.Info.build(post, customUserDetails);
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


//    @Transactional
//    public Long delete(Long postId) {
//        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
//
//        teamRepository.deleteAllByPost(post).orElseThrow(TeamNotFoundException::new);
//        postRepository.deleteById(postId);
//
//        return postId;
//    }

    @Transactional
    public PostResponse.GetPostLike updatePostLike(Long postId, User user) {
        Post post = findPost(postId);
        if (getPostLikeStatus(user, post)) {
            postLikeRepository.deleteByUserAndPost(user, post);
            return PostResponse.GetPostLike.build(false);
        } else {
            postLikeRepository.save(PostLike.create(user, post));
            return PostResponse.GetPostLike.build(true);
        }
    }

    private Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
    }

    private boolean getPostLikeStatus(User user, Post post) {
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
