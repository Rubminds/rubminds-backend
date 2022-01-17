package com.rubminds.api.user.web;

import com.rubminds.api.user.dto.AuthRequest;
import com.rubminds.api.user.dto.AuthResponse;
import com.rubminds.api.user.dto.UserResponse;
import com.rubminds.api.user.security.userdetails.CurrentUser;
import com.rubminds.api.user.security.userdetails.CustomUserDetails;
import com.rubminds.api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse.CreateOrUpdate> signup(@RequestPart(value = "userInfo") AuthRequest.Signup request, @RequestPart(value = "avatar", required = false) MultipartFile file, @CurrentUser CustomUserDetails customUserDetails) {
        AuthResponse.CreateOrUpdate response = userService.signup(request, file, customUserDetails.getUser());
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/update")
    public ResponseEntity<AuthResponse.CreateOrUpdate> update(@RequestPart(value = "userInfo") AuthRequest.Update request, @RequestPart(value = "avatar", required = false) MultipartFile file, @CurrentUser CustomUserDetails customUserDetails) {
        AuthResponse.CreateOrUpdate response = userService.update(request, file, customUserDetails.getUser());
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse.Info> getInfo(@PathVariable Long userId, @CurrentUser CustomUserDetails customUserDetails) {
        UserResponse.Info infoResponse = userService.getUserInfo(userId, customUserDetails.getUser());
        return ResponseEntity.ok().body(infoResponse);
    }

    @GetMapping("/nickname/check")
    public void nicknameCheck(@RequestParam(name = "nickname") String nickname){
        userService.nicknameCheck(nickname);
    }
}
