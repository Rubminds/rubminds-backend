package com.rubminds.api.user.web;

import com.rubminds.api.user.dto.AuthRequest;
import com.rubminds.api.user.dto.AuthResponse;
import com.rubminds.api.user.security.userdetails.CurrentUser;
import com.rubminds.api.user.security.userdetails.CustomUserDetails;
import com.rubminds.api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse.Signup> signup(@CurrentUser CustomUserDetails customUserDetails, @RequestBody AuthRequest.Signup request){
        AuthResponse.Signup signupResponse = userService.signup(customUserDetails, request);
        return ResponseEntity.ok().body(signupResponse);
    }

}
