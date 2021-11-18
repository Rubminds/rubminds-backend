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


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse.Signup> signup(@CurrentUser CustomUserDetails customUserDetails, @RequestBody AuthRequest.Signup request){
        AuthResponse.Signup signupResponse = userService.signup(customUserDetails.getUser().getId(), request);
        return ResponseEntity.ok().body(signupResponse);
    }

    @GetMapping("/mypage")
    public ResponseEntity<UserResponse.Info> mypage(@CurrentUser CustomUserDetails customUserDetails){
        UserResponse.Info mypageResponse = userService.mypage(customUserDetails.getUser().getId());
        return ResponseEntity.ok().body(mypageResponse);
    }

    @GetMapping("/info")
    public ResponseEntity<UserResponse.Info> info(@RequestBody UserRequest.Info request){
        UserResponse.Info infoResponse = userService.info(request);
        return ResponseEntity.ok().body(infoResponse);
    }
}
