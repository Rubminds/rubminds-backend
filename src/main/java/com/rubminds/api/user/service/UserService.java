package com.rubminds.api.user.service;

import com.rubminds.api.file.domain.Avatar;
import com.rubminds.api.file.domain.repository.AvatarRepository;
import com.rubminds.api.file.service.S3Service;
import com.rubminds.api.post.domain.repository.PostRepository;
import com.rubminds.api.skill.domain.Skill;
import com.rubminds.api.skill.domain.UserSkill;
import com.rubminds.api.skill.domain.repository.SkillRepository;
import com.rubminds.api.skill.domain.repository.UserSkillRepository;
import com.rubminds.api.user.domain.User;
import com.rubminds.api.user.domain.repository.UserRepository;
import com.rubminds.api.user.dto.AuthRequest;
import com.rubminds.api.user.dto.AuthResponse;
import com.rubminds.api.user.dto.UserDto;
import com.rubminds.api.user.dto.UserResponse;
import com.rubminds.api.user.exception.DuplicateNicknameException;
import com.rubminds.api.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final PostRepository postRepository;
    private final UserSkillRepository userSkillRepository;
    private final AvatarRepository avatarRepository;
    private final S3Service s3Service;

    @Transactional
    public AuthResponse.CreateOrUpdate signup(AuthRequest.Signup request, MultipartFile file, User user) {
        User findUser = userRepository.findById(user.getId()).orElseThrow(UserNotFoundException::new);
        duplicateNickname(request.getNickname());
        List<Skill> skills = skillRepository.findAllByIdIn(request.getSkillIds());
        List<UserSkill> userSkills = skills.stream().map(skill -> UserSkill.create(findUser, skill)).collect(Collectors.toList());
        Avatar avatar = uploadAvatar(file);
        findUser.signup(request, avatar, userSkills);
        return AuthResponse.CreateOrUpdate.build(findUser);
    }

    public void nicknameCheck(String nickname) {
        duplicateNickname(nickname);
    }

    @Transactional
    public AuthResponse.CreateOrUpdate update(AuthRequest.Update request, MultipartFile file, User user) {
        User findUser = userRepository.findByIdWithAvatar(user.getId()).orElseThrow(UserNotFoundException::new);
        if (request.isNicknameChanged()) {
            duplicateNickname(request.getNickname());
        }
        if (request.isAvatarChanged()) {
            updateAvatar(file, findUser);
        }

        userSkillRepository.deleteAllByUser(findUser);
        List<Skill> skills = skillRepository.findAllByIdIn(request.getSkillIds());
        List<UserSkill> userSkills = skills.stream().map(skill -> UserSkill.create(findUser, skill)).collect(Collectors.toList());

        findUser.update(request, userSkills);
        return AuthResponse.CreateOrUpdate.build(findUser);
    }

    public UserResponse.Info getUserInfo(Long userId, User loginUser) {
        User findUser = userRepository.findByIdWithAvatar(userId).orElseThrow(UserNotFoundException::new);
        List<UserDto.ProjectInfo> projectInfos = postRepository.findCountByStatusAndUser(userId);
        List<UserDto.LikeInfo> likeInfo = postRepository.findCountByLikeAndUser(userId);

        return UserResponse.Info.build(findUser, loginUser, projectInfos, likeInfo);
    }

    private void duplicateNickname(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new DuplicateNicknameException();
        }
    }

    private void updateAvatar(MultipartFile file, User findUser) {
        avatarRepository.deleteById(findUser.getAvatar().getId());
        Avatar avatar = uploadAvatar(file);
        findUser.updateAvatar(avatar);
    }

    private Avatar uploadAvatar(MultipartFile file) {
        if (file == null) {
            return null;
        }
        return avatarRepository.save(Avatar.create(s3Service.upload(file)));
    }
}