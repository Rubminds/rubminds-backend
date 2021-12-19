package com.rubminds.api.user.service;

import com.rubminds.api.file.domain.Avatar;
import com.rubminds.api.file.domain.repository.AvatarRepository;
import com.rubminds.api.file.service.S3Service;
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

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService{
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final UserSkillRepository userSkillRepository;
    private final AvatarRepository avatarRepository;
    private final S3Service s3Service;

    @Transactional
    public AuthResponse.Signup signup(AuthRequest.Update request, MultipartFile file, User user){
        User findUser = findUser(user);
        duplicateNickname(findUser, request.getNickname());
        Avatar avatar = uploadAvatar(file);
        findUser.updateAvatar(avatar);
        findUser.update(request, setUserSkills(request, findUser));
        return AuthResponse.Signup.build(findUser, getAvatarUrl(findUser));
    }

    @Transactional
    public void update(AuthRequest.Update request, MultipartFile file, User user){
        User findUser = findUser(user);
        duplicateNickname(findUser, request.getNickname());
        userSkillRepository.deleteAllByUser(findUser);
        if(findUser.getAvatar()!=null){
            avatarRepository.deleteById(findUser.getAvatar().getId());
        }
        Avatar avatar = uploadAvatar(file);
        findUser.updateAvatar(avatar);
        findUser.update(request, setUserSkills(request, findUser));
    }

    private User findUser(User user){
        return userRepository.findById(user.getId()).orElseThrow(UserNotFoundException::new);
    }

    private void duplicateNickname(User user, String nickname){
        if(userRepository.existsByNickname(nickname)) {
            if(!user.getNickname().equals(nickname)){
                throw new DuplicateNicknameException();
            }
        }
    }

    private List<UserSkill> setUserSkills(AuthRequest.Update request, User user){
        List<UserSkill>userSkills = new ArrayList<>();
        for(Long skillId : request.getSkillIds()){
            Skill findSkill = skillRepository.findById(skillId).orElseThrow(SkillNotFoundException::new);
            UserSkill userSkill = UserSkill.create(user, findSkill);
            userSkills.add(userSkill);
        }
        return userSkills;
    }

    private Avatar uploadAvatar(MultipartFile file){
        if(file == null){
            return null;
        }
        return avatarRepository.save(Avatar.create(s3Service.uploadFile(file)));
    }

    public UserResponse.Info getMe(User user) {
        User findUser = findUser(user);
        return UserResponse.Info.build(findUser, getAvatarUrl(findUser));
    }

    public UserResponse.Info getUserInfo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return UserResponse.Info.build(user, getAvatarUrl(user));
    }

    private String getAvatarUrl(User user){
        String avatarUrl = null;
        if(user.getAvatar()!=null){
            avatarUrl = user.getAvatar().getUrl();
        }
        return avatarUrl;
    }
}