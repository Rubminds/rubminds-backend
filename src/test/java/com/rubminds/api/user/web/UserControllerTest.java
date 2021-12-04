package com.rubminds.api.user.web;

import com.rubminds.MvcTest;
import com.rubminds.api.user.domain.SignupProvider;
import com.rubminds.api.user.domain.User;
import com.rubminds.api.user.dto.AuthRequest;
import com.rubminds.api.user.dto.AuthResponse;
import com.rubminds.api.user.dto.UserRequest;
import com.rubminds.api.user.dto.UserResponse;
import com.rubminds.api.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        AuthRequest.Update request = AuthRequest.Update.builder()
                .nickname("동그라미")
                .introduce("안녕하세요!")
                .job("학생")
                .skillIds(List.of(1L, 2L))
                .build();
        AuthResponse.Update response = AuthResponse.Update.build(user);


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
                                fieldWithPath("job").type(JsonFieldType.STRING).description("직업"),
                                fieldWithPath("skillIds").type(JsonFieldType.ARRAY).description("관심기술")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("유저 식별자"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("job").type(JsonFieldType.STRING).description("직업"),
                                fieldWithPath("introduce").type(JsonFieldType.STRING).description("소개")
                        )
                ));
    }

    @Test
    @DisplayName("게시물 정보입력(수정) 문서화")
    public void updateInfo() throws Exception {
        AuthRequest.Update request = AuthRequest.Update.builder()
                .nickname("동그라미")
                .introduce("안녕하세요!")
                .job("학생")
                .skillIds(List.of(1L, 3L))
                .build();
        AuthResponse.Update response = AuthResponse.Update.build(user);

        given(userService.update(any(), any())).willReturn(response);

        ResultActions results = mvc.perform(post("/api/user/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .characterEncoding("UTF-8")
        );

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("user-update",
                        requestFields(
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("introduce").type(JsonFieldType.STRING).description("소개"),
                                fieldWithPath("job").type(JsonFieldType.STRING).description("직업"),
                                fieldWithPath("skillIds").type(JsonFieldType.ARRAY).description("관심기술")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("유저 식별자"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("job").type(JsonFieldType.STRING).description("직업"),
                                fieldWithPath("introduce").type(JsonFieldType.STRING).description("소개")
                        )
                ));
    }

    @Test
    @DisplayName("게시물 정보조회(내정보) 문서화")
    public void readMyInfo() throws Exception {
        UserResponse.Info response = UserResponse.Info.build(user);

        given(userService.getMe(any())).willReturn(response);

        ResultActions results = mvc.perform(get("/api/user/me")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("user-me",
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("유저 식별자"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("job").type(JsonFieldType.STRING).description("직업"),
                                fieldWithPath("introduce").type(JsonFieldType.STRING).description("소개"),
                                fieldWithPath("userSkills").type(JsonFieldType.ARRAY).description("관심기술"),
                                fieldWithPath("attendLevel").type(JsonFieldType.NUMBER).description("참여도"),
                                fieldWithPath("workLevel").type(JsonFieldType.NUMBER).description("숙련도")
                        )
                ));
    }

    @Test
    @DisplayName("게시물 정보조회(회원정보) 문서화")
    public void readInfo() throws Exception {
        UserRequest.Info request = UserRequest.Info.builder()
                .id(1L)
                .build();
        UserResponse.Info response = UserResponse.Info.build(user);

        given(userService.getUserInfo(any())).willReturn(response);

        ResultActions results = mvc.perform(get("/api/user/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .characterEncoding("UTF-8")
        );

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("user-info",
                        requestFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("유저 식별자")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("유저 식별자"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("job").type(JsonFieldType.STRING).description("직업"),
                                fieldWithPath("introduce").type(JsonFieldType.STRING).description("소개"),
                                fieldWithPath("userSkills").type(JsonFieldType.ARRAY).description("관심기술"),
                                fieldWithPath("attendLevel").type(JsonFieldType.NUMBER).description("참여도"),
                                fieldWithPath("workLevel").type(JsonFieldType.NUMBER).description("숙련도")
                        )
                ));
    }
}