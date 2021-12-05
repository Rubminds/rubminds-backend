package com.rubminds.api.user.web;

import com.rubminds.api.user.dto.AuthRequest;
import com.rubminds.api.user.dto.AuthResponse;
import com.rubminds.api.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/user")
public class AuthController {
    private final AuthService authService;
    //테스트용
    @PostMapping("/login")
    public ResponseEntity<AuthResponse.Login> login(@RequestBody AuthRequest.Login request, HttpServletResponse response) {
        AuthResponse.Login loginResponse = authService.login(request);

        return ResponseEntity.ok().body(loginResponse);
    }
}
