package com.rubminds;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rubminds.api.config.WebMvcConfig;
import com.rubminds.api.user.security.oauth.CustomOAuth2UserService;
import com.rubminds.api.user.security.oauth.OAuth2SuccessHandler;
import com.rubminds.api.user.security.token.JwtAuthEntryPoint;
import com.rubminds.api.user.security.token.JwtProps;
import com.rubminds.api.user.security.token.TokenProvider;
import com.rubminds.api.user.security.userdetails.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureRestDocs
@Import({
        WebMvcConfig.class,
        TokenProvider.class,
        JwtProps.class,
        RestDocsConfig.class,
})
@WithMockCustomUser
public abstract class MvcTest {
    @Autowired
    protected MockMvc mvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @MockBean
    protected CustomUserDetailService customUserDetailService;
    @MockBean
    protected JwtAuthEntryPoint jwtAuthEntryPoint;
    @MockBean
    protected OAuth2SuccessHandler oAuth2SuccessHandler;
    @MockBean
    protected CustomOAuth2UserService customOAuth2UserService;
}

