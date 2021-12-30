package com.rubminds.api.post.service;

import com.rubminds.api.common.dto.PageDto;
import com.rubminds.api.file.service.S3Service;
import com.rubminds.api.post.domain.*;
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
import com.rubminds.api.team.domain.TeamUser;
import com.rubminds.api.team.domain.repository.TeamRepository;
import com.rubminds.api.user.domain.User;
import com.rubminds.api.user.security.userdetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
        TeamUser teamUser = TeamUser.create(user);
        Team team = Team.create(user, teamUser);
        teamRepository.save(team);

        Post post = Post.create(request, team, user);
        Post savedPost = postRepository.save(post);

        createOrUpdatePostAndCustomSKill(request, post);

        if (files != null) {
            List<PostFile> postFiles = s3Service.uploadFileList(files).stream().map(savedFile -> PostFile.create(savedPost, savedFile)).collect(Collectors.toList());
            postFileRepository.saveAll(postFiles);
        }

        return PostResponse.OnlyId.build(savedPost);
    }

    public PostResponse.Info getOne(Long postId, CustomUserDetails customUserDetails) {
        Post post = postRepository.findByIdWithSkillAndUser(postId).orElseThrow(PostNotFoundException::new);
        Integer finishNum = postRepository.FindCountFinish(post);
        return PostResponse.Info.build(post, customUserDetails,finishNum);
    }

    @Transactional
    public PostResponse.OnlyId update(Long postId, PostRequest.CreateOrUpdate request) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        post.update(request);

        postSkillRepository.deleteAllByPost(post);
        customSkillRepository.deleteAllByPost(post);

        createOrUpdatePostAndCustomSKill(request, post);

        return PostResponse.OnlyId.build(post);
    }

    @Transactional
    public void updatePostLike(Long postId, User user) {
        Post post = findPost(postId);
        if (getPostLikeStatus(user, post)) {
            postLikeRepository.deleteByUserAndPost(user, post);
        } else {
            postLikeRepository.save(PostLike.create(user, post));
        }
    }

    public Page<PostResponse.GetList> getList(Kinds kinds, PostStatus postStatus, PageDto pageDto, List<Long> skillId, List<String> customSkillNameList, CustomUserDetails customUserDetails) {
        Page<Post> posts = postRepository.findAllByKindsAndStatus(kinds, postStatus, skillId, customSkillNameList, pageDto.of());
        return posts.map(post -> PostResponse.GetList.build(post, customUserDetails));
    }

    public Page<PostResponse.GetList> getLikePosts(Kinds kinds, PageDto pageDto, CustomUserDetails customUserDetails) {
        Page<Post> posts = postRepository.findAllLikePostByUserId(kinds, customUserDetails.getUser(), pageDto.of());
        return posts.map(post -> PostResponse.GetList.build(post, customUserDetails));
    }

    private void createOrUpdatePostAndCustomSKill(PostRequest.CreateOrUpdate request, Post post) {
        List<Skill> skills = skillRepository.findAllByIdIn(request.getSkillIds());
        List<PostSkill> postSkills = skills.stream().map(skill -> PostSkill.create(skill, post)).collect(Collectors.toList());
        List<CustomSkill> customSkills = request.getCustomSkillName().stream().map(name -> CustomSkill.create(name, post)).collect(Collectors.toList());

        postSkillRepository.saveAll(postSkills);
        customSkillRepository.saveAll(customSkills);
    }

    private Post findPost(Long postId) {
        return postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
    }

    private boolean getPostLikeStatus(User user, Post post) {
        return postLikeRepository.existsByUserAndPost(user, post);
    }

    @Transactional
    public PostResponse.OnlyId updateCompletePost(Long postId, PostRequest.CreateCompletePost request) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        post.updateComplete(request);

        return PostResponse.OnlyId.build(post);
    }
}
