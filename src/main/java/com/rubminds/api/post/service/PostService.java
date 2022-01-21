package com.rubminds.api.post.service;

import com.rubminds.api.common.dto.PageDto;
import com.rubminds.api.file.service.S3Service;
import com.rubminds.api.post.domain.*;
import com.rubminds.api.post.domain.repository.*;
import com.rubminds.api.post.dto.PostRequest;
import com.rubminds.api.post.dto.PostResponse;
import com.rubminds.api.post.exception.NotFullFinishedException;
import com.rubminds.api.post.exception.NotPostStatusFinishedException;
import com.rubminds.api.post.exception.PostNotFoundException;
import com.rubminds.api.post.exception.PostStateException;
import com.rubminds.api.skill.domain.CustomSkill;
import com.rubminds.api.skill.domain.Skill;
import com.rubminds.api.skill.domain.repository.CustomSkillRepository;
import com.rubminds.api.skill.domain.repository.SkillRepository;
import com.rubminds.api.team.domain.Team;
import com.rubminds.api.team.domain.TeamUser;
import com.rubminds.api.team.domain.repository.TeamRepository;
import com.rubminds.api.team.domain.repository.TeamUserRepository;
import com.rubminds.api.user.domain.User;
import com.rubminds.api.user.domain.repository.UserRepository;
import com.rubminds.api.user.exception.UserNotFoundException;
import com.rubminds.api.user.security.userdetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    private final TeamUserRepository teamUserRepository;
    private final UserRepository userRepository;

    @Transactional
    public PostResponse.OnlyId create(PostRequest.Create request, List<MultipartFile> files, User user) {
        TeamUser teamUser = TeamUser.create(user);
        Team team = Team.create(user, teamUser);
        teamRepository.save(team);

        Post post = Post.create(request, team, user);
        Post savedPost = postRepository.save(post);

        createOrUpdatePostAndCustomSKill(request, post);
        saveFiles(files, savedPost, false);

        return PostResponse.OnlyId.build(savedPost);
    }

    public PostResponse.Info getOne(Long postId, CustomUserDetails customUserDetails) {
        Post post = postRepository.findByIdWithSkillAndUser(postId).orElseThrow(PostNotFoundException::new);
        List<PostFile> postFiles = postFileRepository.findAllByPostAndComplete(post, false);
        List<PostFile> completeFiles = postFileRepository.findAllByPostAndComplete(post, true);
        PostFile completeFile = getCompleteFile(completeFiles);
        List<PostFile> completeImages = getImages(completeFiles);
        return PostResponse.Info.build(post, customUserDetails, postFiles, completeFile, completeImages);
    }

    private List<PostFile> getImages(List<PostFile> completeFiles){
        List<PostFile> images = new ArrayList<>();
        for(PostFile completeFile : completeFiles){
            if(completeFile.isImage()){
                images.add(completeFile);
            }
        }
        return images;
    }

    private PostFile getCompleteFile(List<PostFile> completeFiles){
        PostFile file = null;
        for(PostFile completeFile : completeFiles){
            if(!completeFile.isImage()){
                file = completeFile;
            }
        }
        return file;

    }

    @Transactional
    public PostResponse.OnlyId update(Long postId, PostRequest.Create request, List<MultipartFile> files) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        post.update(request);

        postSkillRepository.deleteAllByPost(post);
        customSkillRepository.deleteAllByPost(post);
        postFileRepository.deleteAllByPostAndComplete(post, false);

        createOrUpdatePostAndCustomSKill(request, post);
        saveFiles(files, post, false);

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
    @Transactional
    public PostResponse.OnlyId updateCompletePost(Long postId, PostRequest.CreateCompletePost request, List<MultipartFile> files, User loginUser) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        loginUser.isAdmin(post.getWriter().getId());

        Integer finishNum = postRepository.findCountFinish(post);
        isFinished(post, finishNum);
        isPostStatusFinished(post);

        postFileRepository.deleteAllByPostAndComplete(post, true);
        post.updateComplete(request);
        saveFiles(files, post, true);

        return PostResponse.OnlyId.build(post);
    }

    @Transactional
    public PostResponse.OnlyId changeStatus(Long postId, PostRequest.ChangeStatus request, User loginUser) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        loginUser.isAdmin(post.getWriter().getId());

        if(request.getPostStatus().equals(PostStatus.FINISHED)){
            Integer finishNum = postRepository.findCountFinish(post);
            isFinished(post, finishNum);
        }
        post.changeStatus(request);

        return PostResponse.OnlyId.build(post);
    }

    public Page<PostResponse.GetList> getList(Kinds kinds, PostStatus postStatus, String region, PageDto pageDto, List<Long> skillId, List<String> customSkillNameList, CustomUserDetails customUserDetails) {
        Page<Post> posts = postRepository.findAllByKindsAndStatus(kinds, postStatus, region, skillId, customSkillNameList, pageDto.of());
        return posts.map(post -> PostResponse.GetList.build(post, customUserDetails));
    }

    public PostResponse.GetListByStatus getListByStatus(PostStatus postStatus, Long userId, PageDto pageDto, CustomUserDetails customUserDetails) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Page<Post> posts = postRepository.findAllByStatusAndUser(postStatus, userId, pageDto.of());
        return PostResponse.GetListByStatus.build(user, posts, customUserDetails);
    }

    public Page<PostResponse.GetList> getLikePosts(Kinds kinds, PageDto pageDto, CustomUserDetails customUserDetails) {
        Page<Post> posts = postRepository.findAllLikePostByUserId(kinds, customUserDetails.getUser(), pageDto.of());
        return posts.map(post -> PostResponse.GetList.build(post, customUserDetails));
    }

    public PostResponse.OnlyId delete(Long postId, User loginUser){
        Post post = findPost(postId);
        loginUser.isAdmin(post.getWriter().getId());
        if(post.getPostStatus().equals(PostStatus.RECRUIT)||post.getPostStatus().equals(PostStatus.WORKING)){
            postRepository.deleteById(postId);
        }else{
            throw new PostStateException();
        }
        return PostResponse.OnlyId.build(post);

    }

    private void saveFiles(List<MultipartFile> files, Post savedPost, boolean complete) {
        if (files != null) {
            List<PostFile> postFiles = s3Service.uploadFileList(files).stream().map(savedFile -> PostFile.create(savedPost, savedFile, complete)).collect(Collectors.toList());
            postFileRepository.saveAll(postFiles);
        }
    }

    private void createOrUpdatePostAndCustomSKill(PostRequest.Create request, Post post) {
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

    public void isFinished(Post post, Integer finishNum) {
        Team team = post.getTeam();
        Integer teamUserCnt = teamUserRepository.countAllByTeam(team);
        if (!Objects.equals(teamUserCnt, finishNum)) throw new NotFullFinishedException();
    }

    public void isPostStatusFinished(Post post) {
        if (!Objects.equals(post.getPostStatus(),PostStatus.FINISHED)) throw new NotPostStatusFinishedException();
    }

}
