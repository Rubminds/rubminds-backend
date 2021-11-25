package com.rubminds.api.user.web;

import com.rubminds.MvcTest;
import com.rubminds.api.post.domain.Post;
import com.rubminds.api.post.domain.PostEnumClass.Kinds;
import com.rubminds.api.post.domain.PostEnumClass.Meeting;
import com.rubminds.api.post.domain.PostEnumClass.PostStatus;
import com.rubminds.api.post.domain.PostEnumClass.Region;
import com.rubminds.api.post.dto.PostRequest;
import com.rubminds.api.post.dto.PostResponse;
import com.rubminds.api.post.service.PostService;
import com.rubminds.api.post.web.PostController;
import com.rubminds.api.user.domain.SignupProvider;
import com.rubminds.api.user.domain.User;

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
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("게시물 정보입력(생성) 문서화")
@WebMvcTest(PostController.class)
public class PostControllerTest extends MvcTest {

    @MockBean
    private PostService postService;

    private User user;
    private Post post;

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

        post = Post.builder()
                .id(1L)
                .title("테스트")
                .content("내용")
                .region(Region.서울)
                .postStatus(PostStatus.GATHERING)
                .kinds(Kinds.PROJECT)
                .headcount(3)
                .meeting(Meeting.BOTH)
                .writer(user)
                .build();
    }

    @Test
    @DisplayName("게시물 정보입력(생성) 문서화")
    public void cretePost() throws Exception {
        PostRequest.Create request = PostRequest.Create.builder()
                .writer(user.getNickname())
                .title("테스트")
                .content("내용")
                .region(Region.서울)
                .postsStatus(PostStatus.GATHERING)
                .kinds(Kinds.PROJECT)
                .headcount(3)
                .meeting(Meeting.BOTH)
                .build();

        PostResponse.OnlyId response = PostResponse.OnlyId.build(post);
        given(postService.create(any(), any())).willReturn(response);

        ResultActions results = mvc.perform(post("/api/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .characterEncoding("UTF-8")
        );

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post_create",
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                                fieldWithPath("region").type(JsonFieldType.STRING).description("지역"),
                                fieldWithPath("postsStatus").type(JsonFieldType.STRING).description("진행상태"),
                                fieldWithPath("kinds").type(JsonFieldType.STRING).description("글종류"),
                                fieldWithPath("headcount").type(JsonFieldType.STRING).description("모집인원"),
                                fieldWithPath("meeting").type(JsonFieldType.STRING).description("미팅방법"),
                                fieldWithPath("writer").type(JsonFieldType.STRING).description("작성자 닉네임")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("게시글 식별자")
                        )
                ));
    }

    @Test
    @DisplayName("게시물 수정 문서화")
    public void updatePost() throws Exception {
        PostRequest.Create request = PostRequest.Create.builder()
                .writer(user.getNickname())
                .title("테스트")
                .content("내용")
                .region(Region.서울)
                .postsStatus(PostStatus.GATHERING)
                .kinds(Kinds.PROJECT)
                .headcount(3)
                .meeting(Meeting.BOTH)
                .build();

        PostResponse.OnlyId response = PostResponse.OnlyId.build(post);
        given(postService.create(any(), any())).willReturn(response);

        ResultActions results = mvc.perform(put("/api/post/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .characterEncoding("UTF-8")
        );

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post_update",
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                                fieldWithPath("region").type(JsonFieldType.STRING).description("지역"),
                                fieldWithPath("postsStatus").type(JsonFieldType.STRING).description("진행상태"),
                                fieldWithPath("kinds").type(JsonFieldType.STRING).description("글종류"),
                                fieldWithPath("headcount").type(JsonFieldType.NUMBER).description("모집인원"),
                                fieldWithPath("meeting").type(JsonFieldType.STRING).description("미팅방법"),
                                fieldWithPath("writer").type(JsonFieldType.STRING).description("작성자 닉네임")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("게시글 식별자")
                        )
                ));
    }
}
