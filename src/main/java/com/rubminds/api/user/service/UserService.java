package com.rubminds.api.user.service;

import com.rubminds.api.skill.Exception.SkillNotFoundException;
import com.rubminds.api.skill.domain.Skill;
import com.rubminds.api.skill.domain.repository.SkillRepository;
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

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final SkillRepository skillRepository;

    @Transactional
    public AuthResponse.Signup signup(AuthRequest.Signup request, User user) {
        User findUser = userRepository.findById(user.getId()).orElseThrow(UserNotFoundException::new);
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new DuplicateNicknameException();
        }
        Skill skill = skillRepository.findById(request.getSkillId()).orElseThrow(SkillNotFoundException::new);
        findUser.signup(request,skill);
        return AuthResponse.Signup.build(findUser);
    }

    public UserResponse.Info getMe(User user) {
        return UserResponse.Info.build(user);
    }

    public UserResponse.Info getUserInfo(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return UserResponse.Info.build(user);
    }
}