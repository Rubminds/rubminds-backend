package com.rubminds.api.user.web;

import com.rubminds.api.user.dto.AuthRequest;
import com.rubminds.api.user.dto.AuthResponse;
import com.rubminds.api.user.dto.UserRequest;
import com.rubminds.api.user.dto.UserResponse;
import com.rubminds.api.user.security.userdetails.CurrentUser;
import com.rubminds.api.user.security.userdetails.CustomUserDetails;
import com.rubminds.api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse.Signup> signup(@CurrentUser CustomUserDetails customUserDetails, @RequestBody AuthRequest.Signup request, HttpServletResponse response){
        AuthResponse.Signup signupResponse = userService.signup(customUserDetails, request);
        return ResponseEntity.ok().body(signupResponse);
    }

    @GetMapping("/mypage")
    public ResponseEntity<UserResponse.Info> mypage(@CurrentUser CustomUserDetails customUserDetails, HttpServletResponse response){
        UserResponse.Info mypageResponse = userService.mypage(customUserDetails);
        return ResponseEntity.ok().body(mypageResponse);
    }

    @GetMapping("/info")
    public ResponseEntity<UserResponse.Info> info(@RequestBody UserRequest.Info request, HttpServletResponse response){
        UserResponse.Info infoResponse = userService.info(request);
        return ResponseEntity.ok().body(infoResponse);
    }
}
