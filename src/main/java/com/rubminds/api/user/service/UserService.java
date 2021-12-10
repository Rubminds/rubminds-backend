package com.rubminds.api.user.service;

import com.rubminds.api.file.domain.Avatar;
import com.rubminds.api.file.service.FileService;
import com.rubminds.api.skill.exception.SkillNotFoundException;
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
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final UserSkillRepository userSkillRepository;
    private final FileService fileService;

    public AuthRequest.Update insertUpdate(MultipartHttpServletRequest multipartHttpServletRequest){
        return AuthRequest.Update.builder()
                .nickname(multipartHttpServletRequest.getParameter("nickname"))
                .introduce(multipartHttpServletRequest.getParameter("introduce"))
                .job(multipartHttpServletRequest.getParameter("job"))
                .skillIds(multipartHttpServletRequest.getParameter("skillIds"))
                .isAvatarChanged(multipartHttpServletRequest.getParameter("isAvatarChanged"))
                .build();
    }

    @Transactional
    public AuthResponse.Update signup(AuthRequest.Update request, MultipartFile file, User user){
        User findUser = findUser(user);
        duplicateNickname(request.getNickname());
        boolean isAvatarChanged = Boolean.valueOf(request.getIsAvatarChanged());
        Avatar avatar = null;
        if(isAvatarChanged) {
            if (file != null) {
                avatar = fileService.uploadAvatar(file);
            }
            findUser.updateAvatar(avatar);
        }
        findUser.update(request, setUserSkills(request, findUser));
        return AuthResponse.Update.build(findUser);
    }

    @Transactional
    public AuthResponse.Update update(AuthRequest.Update request, MultipartFile file, User user){
        User findUser = findUser(user);
        duplicateNickname(request.getNickname());
        userSkillRepository.deleteAllByUser(findUser);
        boolean isAvatarChanged = Boolean.valueOf(request.getIsAvatarChanged());
        if(isAvatarChanged){
            Avatar avatar = null;
            if(user.getAvatar() != null){
                fileService.deleteByAvatarId(findUser.getAvatar().getId());
            }
            if(file != null){
                avatar = fileService.uploadAvatar(file);
            }
            findUser.updateAvatar(avatar);
        }
        findUser.update(request, setUserSkills(request, findUser));
        return AuthResponse.Update.build(findUser);
    }

    private User findUser(User user){
        return userRepository.findById(user.getId()).orElseThrow(UserNotFoundException::new);
    }

    private void duplicateNickname(String nickname){
        if(userRepository.existsByNickname(nickname)) {
            throw new DuplicateNicknameException();
        }
    }

    private List<UserSkill> setUserSkills(AuthRequest.Update request, User user){
        List<Long> skillIds = Arrays.stream(request.getSkillIds().split(",")).map(Long::parseLong).collect(Collectors.toList());
        List<UserSkill> userSkills = new ArrayList<>();
        for(Long skillId : skillIds){
            Skill findSkill = skillRepository.findById(skillId).orElseThrow(SkillNotFoundException::new);
            UserSkill userSkill = UserSkill.create(user, findSkill);
            userSkills.add(userSkill);
        }
        return userSkills;
    }

    public UserResponse.Info getMe(User user) {
        return UserResponse.Info.build(findUser(user));
    }

    public UserResponse.Info getUserInfo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return UserResponse.Info.build(user);
    }
}