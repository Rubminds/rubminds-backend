package com.rubminds.api.user.web;

import com.rubminds.MvcTest;
import com.rubminds.api.file.domain.Avatar;
import com.rubminds.api.file.dto.FileDTO;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("유저 API 문서화")
@WebMvcTest(UserController.class)
class UserControllerTest extends MvcTest {

    @MockBean
    private UserService userService;

    private User user;

    private Avatar avatar;

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

        avatar = Avatar.create(FileDTO.Upload.builder()
                .originalFileName("white.jpeg")
                .name("cb3ee9d9-f005-46c3-85b8-b6acf630dcb6.jpeg")
                .extension(".jpeg")
                .size(7695L)
                .publicUrl("https://rubminds.s3.ap-northeast-2.amazonaws.com/cb3ee9d9-f005-46c3-85b8-b6acf630dcb6.jpeg")
                .width(225)
                .height(300)
                .build());
    }

    @Test
    @DisplayName("게시물 정보입력(생성) 문서화")
    public void createInfo() throws Exception {
        InputStream inputStream = new ClassPathResource("dummy/image/white.jpeg").getInputStream();
        MockMultipartFile mockAvatar = new MockMultipartFile("avatar", "white.jpeg", "image/jpeg", inputStream.readAllBytes());
        String content = objectMapper.writeValueAsString(new AuthRequest.Update("동그라미", "학생", "안녕하세요!", List.of(2L, 6L),true));
        MockMultipartFile mockUserInfo = new MockMultipartFile("userInfo", "jsondata","application/json",content.getBytes(StandardCharsets.UTF_8));

        AuthResponse.Update response = AuthResponse.Update.build(user);

        given(userService.signup(any(), any(), any())).willReturn(response);

        ResultActions results = mvc.perform(
                multipart("/api/user/signup")
                        .file(mockUserInfo)
                        .file(mockAvatar)
                        .contentType(MediaType.MULTIPART_MIXED)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("user-signup",
                        requestParts(
                                partWithName("avatar").description("프로필이미지 - 없으면 null"),
                                partWithName("userInfo").description("유저 정보 - JSON")
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
        InputStream inputStream = new ClassPathResource("dummy/image/white.jpeg").getInputStream();
        MockMultipartFile mockAvatar = new MockMultipartFile("avatar", "white.jpeg", "image/jpeg", inputStream.readAllBytes());
        String content = objectMapper.writeValueAsString(new AuthRequest.Update("동그라미", "학생", "안녕하세요!", List.of(1L, 3L),true));
        MockMultipartFile mockUserInfo = new MockMultipartFile("userInfo", "jsondata","application/json",content.getBytes(StandardCharsets.UTF_8));

        AuthResponse.Update response = AuthResponse.Update.build(user);

        given(userService.update(any(), any(), any())).willReturn(response);

        ResultActions results = mvc.perform(
                multipart("/api/user/update")
                        .file(mockUserInfo)
                        .file(mockAvatar)
                        .contentType(MediaType.MULTIPART_MIXED)
                        .accept(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8")
        );

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("user-update",
                        requestParts(
                                partWithName("avatar").description("프로필이미지 - 없으면 null"),
                                partWithName("userInfo").description("유저 정보 - JSON")
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
        User user = User.builder()
                .id(1L)
                .oauthId("1")
                .avatar(avatar)
                .nickname("동그라미")
                .job("학생")
                .introduce("안녕하세요!")
                .provider(SignupProvider.RUBMINDS)
                .signupCheck(true)
                .build();

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
                                fieldWithPath("userSkills").type(JsonFieldType.ARRAY).description("관심기술 - [{\"userSkillId\": id,\"name\": name}]"),
                                fieldWithPath("attendLevel").type(JsonFieldType.NUMBER).description("참여도"),
                                fieldWithPath("workLevel").type(JsonFieldType.NUMBER).description("숙련도"),
                                fieldWithPath("avatar").type(JsonFieldType.STRING).description("프로필이미지Url")
                        )
                ));
    }

    @Test
    @DisplayName("게시물 정보조회(회원정보) 문서화")
    public void readInfo() throws Exception {
        User user = User.builder()
                .id(1L)
                .oauthId("1")
                .avatar(avatar)
                .nickname("동그라미")
                .job("학생")
                .introduce("안녕하세요!")
                .provider(SignupProvider.RUBMINDS)
                .signupCheck(true)
                .build();

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
                                fieldWithPath("userSkills").type(JsonFieldType.ARRAY).description("관심기술 - [{\"userSkillId\": id,\"name\": name}]"),
                                fieldWithPath("attendLevel").type(JsonFieldType.NUMBER).description("참여도"),
                                fieldWithPath("workLevel").type(JsonFieldType.NUMBER).description("숙련도"),
                                fieldWithPath("avatar").type(JsonFieldType.STRING).description("프로필이미지Url")
                        )
                ));
    }
}