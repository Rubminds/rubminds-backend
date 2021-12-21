package com.rubminds.api.user.service;

import com.rubminds.api.file.domain.Avatar;
import com.rubminds.api.file.domain.repository.AvatarRepository;
import com.rubminds.api.file.service.S3Service;
import com.rubminds.api.skill.domain.Skill;
import com.rubminds.api.skill.domain.UserSkill;
import com.rubminds.api.skill.domain.repository.SkillRepository;
import com.rubminds.api.skill.domain.repository.UserSkillRepository;
import com.rubminds.api.user.domain.User;
import com.rubminds.api.user.domain.repository.UserRepository;
import com.rubminds.api.user.dto.AuthRequest;
import com.rubminds.api.user.dto.AuthResponse;
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
    private final UserSkillRepository userSkillRepository;
    private final AvatarRepository avatarRepository;
    private final S3Service s3Service;

    @Transactional
    public AuthResponse.Signup signup(AuthRequest.Signup request, MultipartFile file, User user) {
        User findUser = userRepository.findById(user.getId()).orElseThrow(UserNotFoundException::new);
        duplicateNickname(request.getNickname());
        List<Skill> skills = skillRepository.findAllByIdIn(request.getSkillIds());
        List<UserSkill> userSkills = skills.stream().map(skill -> UserSkill.create(findUser, skill)).collect(Collectors.toList());
        Avatar avatar = uploadAvatar(file);
        findUser.signup(request, avatar, userSkills);
        return AuthResponse.Signup.build(findUser, avatar);
    }

    @Transactional
    public void update(AuthRequest.Update request, MultipartFile file, User user) {
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
    }


    public UserResponse.Info getMe(User user) {
        User findUser = userRepository.findById(user.getId()).orElseThrow(UserNotFoundException::new);
        return UserResponse.Info.build(findUser, getAvatarUrl(findUser));
    }

    public UserResponse.Info getUserInfo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return UserResponse.Info.build(user, getAvatarUrl(user));
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

    private String getAvatarUrl(User user) {
        String avatarUrl = null;
        if (user.getAvatar() != null) {
            avatarUrl = user.getAvatar().getUrl();
        }
        return avatarUrl;
    }
}