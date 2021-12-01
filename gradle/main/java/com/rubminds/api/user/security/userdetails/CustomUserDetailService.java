package com.rubminds.api.user.security.userdetails;

import com.rubminds.api.user.domain.User;
import com.rubminds.api.user.domain.repository.UserRepository;
import com.rubminds.api.user.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String nickname) {
        User user = userRepository.findByNickname(nickname).orElseThrow(UserNotFoundException::new);
        return CustomUserDetails.create(user);
    }

    public UserDetails loadUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return CustomUserDetails.create(user);
    }
}
