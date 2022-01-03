package com.rubminds.api.user.security.oauth;

import com.rubminds.api.user.domain.User;
import com.rubminds.api.user.domain.repository.UserRepository;
import com.rubminds.api.user.exception.UserNotFoundException;
import com.rubminds.api.user.security.token.Token;
import com.rubminds.api.user.security.token.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@Transactional
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    @Value("${oauth2.success.redirect.url}")
    private String url;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String oAuthId;
        if (oauth2User.getAttributes().get("sub") != null) {
            oAuthId = String.valueOf(oauth2User.getAttributes().get("sub"));
        } else {
            oAuthId = String.valueOf(oauth2User.getAttributes().get("id"));
        }
        User user = userRepository.findByOauthId(oAuthId).orElseThrow(UserNotFoundException::new);

        Token token = tokenProvider.generateAccessToken(user);

        String nickname = user.getNickname();
        if (nickname != null) {
            nickname = URLEncoder.encode(nickname, StandardCharsets.UTF_8);
        }
        String avatar = null;
        if (user.getAvatar() != null) {
            avatar = user.getAvatar().getUrl().substring(8);
        }

        if (user.getAvatar() != null) {
            response.sendRedirect(url + user.getId() + "/" + nickname + "/" + token.getToken() + "/" + user.isSignupCheck() + "/" + avatar);
        } else {
            response.sendRedirect(url + user.getId() + "/" + nickname + "/" + token.getToken() + "/" + user.isSignupCheck());
        }
    }
}
