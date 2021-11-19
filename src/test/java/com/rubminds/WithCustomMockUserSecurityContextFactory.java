package com.rubminds;

import com.rubminds.api.user.domain.User;
import com.rubminds.api.user.security.userdetails.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithCustomMockUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        User user = User
                .builder()
                .oauthId("1")
                .nickname("테스터")
                .build();

        CustomUserDetails customUserDetails = CustomUserDetails.create(user);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        context.setAuthentication(token);
        return context;
    }
}
