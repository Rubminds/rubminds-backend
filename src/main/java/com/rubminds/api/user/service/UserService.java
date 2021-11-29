package com.rubminds.api.user.service;

import com.rubminds.api.skill.Exception.SkillNotFoundException;
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

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;
    private final UserSkillRepository userSkillRepository;

    @Transactional
    public AuthResponse.Update signup(AuthRequest.Update request, User user){
        User findUser = findUser(user);
        duplicateNickname(request.getNickname());
        findUser.update(request, setUserSkills(request, findUser));
        return AuthResponse.Update.build(findUser);
    }

    @Transactional
    public AuthResponse.Update update(AuthRequest.Update request, User user){
        User findUser = findUser(user);
        duplicateNickname(request.getNickname());
        userSkillRepository.deleteAllByUser(findUser);
        findUser.update(request, setUserSkills(request, findUser));
        return AuthResponse.Update.build(findUser);
    }

    public User findUser(User user){
        User findUser = userRepository.findById(user.getId()).orElseThrow(UserNotFoundException::new);
        return findUser;
    }

    public void duplicateNickname(String nickname){
        if(userRepository.existsByNickname(nickname)) {
            throw new DuplicateNicknameException();
        }
    }

    public List<UserSkill> setUserSkills(AuthRequest.Update request, User user){
        List<UserSkill> userSkills = new ArrayList<>();
        for(Long skillId : request.getSkillIds()){
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