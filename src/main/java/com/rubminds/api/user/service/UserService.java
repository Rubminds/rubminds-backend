package com.rubminds.api.user.service;

import com.rubminds.api.user.domain.User;
import com.rubminds.api.user.domain.repository.UserRepository;
import com.rubminds.api.user.dto.AuthRequest;
import com.rubminds.api.user.dto.AuthResponse;
import com.rubminds.api.user.dto.UserRequest;
import com.rubminds.api.user.dto.UserResponse;
import com.rubminds.api.user.exception.DuplicateNicknameException;
import com.rubminds.api.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public AuthResponse.Signup signup(Long id, AuthRequest.Signup request){
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        if(userRepository.existsByNickname(request.getNickname())){
            throw new DuplicateNicknameException();
        }
        user.signup(request);
        return AuthResponse.Signup.build(user.getId(), user.getNickname(), user.getJob(), user.getIntroduce());
    }

    public UserResponse.Info mypage(Long id){
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return UserResponse.Info.build(user.getId(), user.getNickname(), user.getJob(), user.getIntroduce());
    }
    public UserResponse.Info info(UserRequest.Info request){
        User user = userRepository.findById(request.getId()).orElseThrow(UserNotFoundException::new);
        return UserResponse.Info.build(user.getId(), user.getNickname(), user.getJob(), user.getIntroduce());
    }



}

