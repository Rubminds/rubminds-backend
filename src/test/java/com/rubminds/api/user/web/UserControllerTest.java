package com.rubminds.api.user.web;

import com.rubminds.MvcTest;
import com.rubminds.api.user.domain.SignupProvider;
import com.rubminds.api.user.domain.User;
import com.rubminds.api.user.dto.AuthRequest;
import com.rubminds.api.user.dto.AuthResponse;
import com.rubminds.api.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("유저 API 문서화")
@WebMvcTest(UserController.class)
class UserControllerTest extends MvcTest {

    @MockBean
    private UserService userService;

    private User user;

    @BeforeEach
    public void setup() {
        user = User.builder()
                .id(1L)
                .oauthId("1")
                .nickname("동그라미")
                .job("학생")
                .introduce("안녕하세요!")
                .provider(SignupProvider.RUBMINDS)
                .signupCheck(true)
                .build();
    }

    @Test
    @DisplayName("게시물 정보입력(생성) 문서화")
    public void createInfo() throws Exception {
        AuthRequest.Signup request = AuthRequest.Signup.builder()
                .nickname("동그라미")
                .introduce("안녕하세요!")
                .job("학생")
                .build();
        AuthResponse.Signup response = AuthResponse.Signup.build(user);

        given(userService.signup(any(), any())).willReturn(response);

        ResultActions results = mvc.perform(post("/api/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .characterEncoding("UTF-8")
        );

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("user-signup",
                        requestFields(
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("introduce").type(JsonFieldType.STRING).description("소개"),
                                fieldWithPath("job").type(JsonFieldType.STRING).description("직업")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("유저 식별자"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("job").type(JsonFieldType.STRING).description("직업"),
                                fieldWithPath("introduce").type(JsonFieldType.STRING).description("소개")
                        )
                ));
    }
}