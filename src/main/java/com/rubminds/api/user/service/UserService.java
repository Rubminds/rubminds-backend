package com.rubminds.api.user.service;

import com.rubminds.api.user.domain.User;
import com.rubminds.api.user.domain.repository.UserRepository;
import com.rubminds.api.user.dto.AuthRequest;
import com.rubminds.api.user.dto.AuthResponse;
import com.rubminds.api.user.dto.UserRequest;
import com.rubminds.api.user.dto.UserResponse;
import com.rubminds.api.user.exception.DuplicateNicknameException;
import com.rubminds.api.user.exception.UserNotFoundException;
import com.rubminds.api.user.security.userdetails.CurrentUser;
import com.rubminds.api.user.security.userdetails.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public AuthResponse.Signup signup(@CurrentUser CustomUserDetails customUserDetails, AuthRequest.Signup request){
        User user = userRepository.findById(customUserDetails.getUser().getId()).orElseThrow(UserNotFoundException::new);
        if(userRepository.existsByNickname(request.getNickname())){
            throw new DuplicateNicknameException();
        }
        user.update(request);
        return AuthResponse.Signup.build(user.getId(), user.getNickname(), user.getJob(), user.getIntroduce() ,user.isSignupCheck());
    }

    public UserResponse.Info mypage(@CurrentUser CustomUserDetails customUserDetails){
        User user = userRepository.findById(customUserDetails.getUser().getId()).orElseThrow(UserNotFoundException::new);
        return UserResponse.Info.build(user.getId(), user.getNickname(), user.getJob(), user.getIntroduce());
    }
    public UserResponse.Info info(UserRequest.Info request){
        User user = userRepository.findById(request.getId()).orElseThrow(UserNotFoundException::new);
        return UserResponse.Info.build(user.getId(), user.getNickname(), user.getJob(), user.getIntroduce());
    }



}

