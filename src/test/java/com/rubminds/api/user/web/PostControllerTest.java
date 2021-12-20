package com.rubminds.api.user.web;

import com.rubminds.MvcTest;
import com.rubminds.api.post.domain.Kinds;
import com.rubminds.api.post.domain.Meeting;
import com.rubminds.api.post.domain.Post;
import com.rubminds.api.post.domain.PostStatus;
import com.rubminds.api.post.dto.PostRequest;
import com.rubminds.api.post.dto.PostResponse;
import com.rubminds.api.post.service.PostService;
import com.rubminds.api.post.web.PostController;
import com.rubminds.api.skill.domain.CustomSkill;
import com.rubminds.api.skill.domain.Skill;
import com.rubminds.api.team.domain.Team;
import com.rubminds.api.user.domain.SignupProvider;
import com.rubminds.api.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
                .region("서울")
                .postStatus(PostStatus.RECRUIT)
                .kinds(Kinds.PROJECT)
                .headcount(3)
                .meeting(Meeting.BOTH)
                .writer(user)
                .customSkills(Collections.singletonList(CustomSkill.builder().id(1L).name("java").build()))
                .build();
    }

    @Test
    @DisplayName("프로젝트, 스터디 게시물 정보입력(생성) 문서화")
    public void createProjectAndScoutPost() throws Exception {
        PostRequest.CreateOrUpdate request = PostRequest.CreateOrUpdate.builder()
                .title("테스트")
                .content("내용")
                .region("서울")
                .kinds(Kinds.PROJECT)
                .headcount(3)
                .meeting(Meeting.BOTH)
                .skillIds(List.of(1L, 2L))
                .customSkillName(List.of("firebase", "unity"))
                .build();

        PostResponse.OnlyId response = PostResponse.OnlyId.build(post);
        given(postService.createRecruitProjectOrStudy(any(), any())).willReturn(response);

        ResultActions results = mvc.perform(post("/api/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .characterEncoding("UTF-8")
        );

        results.andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("post_create_project_study",
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                                fieldWithPath("region").type(JsonFieldType.STRING).description("지역"),
                                fieldWithPath("headcount").type(JsonFieldType.NUMBER).description("모집인원"),
                                fieldWithPath("kinds").type(JsonFieldType.STRING).description("게시물 종류"),
                                fieldWithPath("meeting").type(JsonFieldType.STRING).description("미팅방법(ONLINE, OFFLINE, BOTH)"),
                                fieldWithPath("skillIds").type(JsonFieldType.ARRAY).description("게시물지정스킬목록"),
                                fieldWithPath("customSkillName").type(JsonFieldType.ARRAY).description("키타스킬목록지정")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("게시글 식별자")
                        )
                ));
    }


    @Test
    @DisplayName("스카웃 게시물 정보입력(생성) 문서화")
    public void createScoutPost() throws Exception {
        PostRequest.CreateOrUpdate request = PostRequest.CreateOrUpdate.builder()
                .title("테스트")
                .content("내용")
                .region("서울")
                .kinds(Kinds.SCOUT)
                .headcount(3)
                .meeting(Meeting.BOTH)
                .skillIds(List.of(1L, 2L))
                .customSkillName(List.of("firebase", "unity"))
                .build();

        PostResponse.OnlyId response = PostResponse.OnlyId.build(post);
        given(postService.createRecruitScout(any(), any())).willReturn(response);

        ResultActions results = mvc.perform(post("/api/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .characterEncoding("UTF-8")
        );

        results.andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("post_create_scout",
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                                fieldWithPath("region").type(JsonFieldType.STRING).description("지역"),
                                fieldWithPath("kinds").type(JsonFieldType.STRING).description("게시물 종류"),
                                fieldWithPath("headcount").type(JsonFieldType.NUMBER).description("모집인원"),
                                fieldWithPath("meeting").type(JsonFieldType.STRING).description("미팅방법(ONLINE, OFFLINE, BOTH)"),
                                fieldWithPath("skillIds").type(JsonFieldType.ARRAY).description("게시물지정스킬목록"),
                                fieldWithPath("customSkillName").type(JsonFieldType.ARRAY).description("키타스킬목록지정")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("게시글 식별자")
                        )
                ));
    }

    @Test
    @DisplayName("게시물 수정 문서화")
    public void updatePost() throws Exception {
        PostRequest.CreateOrUpdate request = PostRequest.CreateOrUpdate.builder()
                .title("테스트")
                .content("내용")
                .region("Seoul")
                .kinds(Kinds.PROJECT)
                .headcount(3)
                .meeting(Meeting.BOTH)
                .skillIds(List.of(1L, 2L))
                .customSkillName(List.of("firebase", "unity"))
                .build();

        PostResponse.OnlyId response = PostResponse.OnlyId.build(post);
        given(postService.update(any(), any())).willReturn(response);

        ResultActions results = mvc.perform(RestDocumentationRequestBuilders
                .put("/api/post/{postId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .characterEncoding("UTF-8")
        );

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post_update",
                        pathParameters(
                                parameterWithName("postId").description("게시물 식별자")
                        ),
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                                fieldWithPath("region").type(JsonFieldType.STRING).description("지역"),
                                fieldWithPath("kinds").type(JsonFieldType.STRING).description("글종류"),
                                fieldWithPath("headcount").type(JsonFieldType.NUMBER).description("모집인원"),
                                fieldWithPath("meeting").type(JsonFieldType.STRING).description("미팅방법"),
                                fieldWithPath("skillIds").type(JsonFieldType.ARRAY).description("게시물지정스킬목록"),
                                fieldWithPath("customSkillName").type(JsonFieldType.ARRAY).description("기타스킬목록지정")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("게시글 식별자")
                        )
                ));
    }

    @Test
    @DisplayName("게시물 삭제 문서화")
    public void deletePost() throws Exception {
        ResultActions results = mvc.perform(RestDocumentationRequestBuilders.
                delete("/api/post/{postId}", 1));

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post_delete",
                        pathParameters(
                                parameterWithName("postId").description("게시물 식별자")
                        )
                ));
    }


    @Test
    @DisplayName("게시물 디테일 조회 문서화")
    public void detailPost() throws Exception {
        Skill skill1 = Skill.builder().id(1L).name("spring").build();
        Skill skill2 = Skill.builder().id(2L).name("react").build();
        List<Skill> skills = Arrays.asList(skill1, skill2);
        Team team = Team.builder().id(1L).post(post).admin(user).build();
        PostResponse.Info response = PostResponse.Info.build(post, skills, team);

        given(postService.getPost(any(), any())).willReturn(response);

        ResultActions results = mvc.perform(RestDocumentationRequestBuilders
                .get("/api/post/{postId}", 1L));

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post_detail",
                        pathParameters(
                                parameterWithName("postId").description("게시물 식별자")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("게시글식별자"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                                fieldWithPath("region").type(JsonFieldType.STRING).description("지역"),
                                fieldWithPath("postsStatus").type(JsonFieldType.STRING).description("진행상태"),
                                fieldWithPath("kinds").type(JsonFieldType.STRING).description("글종류"),
                                fieldWithPath("headcount").type(JsonFieldType.NUMBER).description("모집인원"),
                                fieldWithPath("meeting").type(JsonFieldType.STRING).description("미팅방법"),
                                fieldWithPath("writer").type(JsonFieldType.STRING).description("작성자 닉네임"),
                                fieldWithPath("postSkills.[].id").type(JsonFieldType.NUMBER).description("게시물지정스킬목록 식별자"),
                                fieldWithPath("postSkills.[].name").type(JsonFieldType.STRING).description("게시물지정스킬 이름"),
                                fieldWithPath("customSkills.[].id").type(JsonFieldType.NUMBER).description("게시물지정스킬목록 식별자"),
                                fieldWithPath("customSkills.[].name").type(JsonFieldType.STRING).description("게시물지정스킬 이름"),
                                fieldWithPath("teamId").type(JsonFieldType.NUMBER).description("게시물지정스킬 이름")

                        )
                ));

    }

    @Test
    @DisplayName("게시물 목록 조회 문서화")
    public void getPosts() throws Exception {
        List<PostResponse.GetPost> posts = new ArrayList<>();
        PostResponse.GetPost post1 = PostResponse.GetPost.build(post, true);
        PostResponse.GetPost post2 = PostResponse.GetPost.build(post, false);
        posts.add(post1);
        posts.add(post2);

        PostResponse.GetPosts response = PostResponse.GetPosts.build(posts);

        given(postService.getPosts(any())).willReturn(response);

        ResultActions results = mvc.perform(get("/api/posts", user)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post_posts",
                        responseFields(
                                fieldWithPath("posts").type(JsonFieldType.ARRAY).description("게시글 목록"),
                                fieldWithPath("posts[].id").type(JsonFieldType.NUMBER).description("게시글식별자"),
                                fieldWithPath("posts[].title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("posts[].region").type(JsonFieldType.STRING).description("지역"),
                                fieldWithPath("posts[].postStatus").type(JsonFieldType.STRING).description("진행상태"),
                                fieldWithPath("posts[].kinds").type(JsonFieldType.STRING).description("글종류"),
                                fieldWithPath("posts[].meeting").type(JsonFieldType.STRING).description("미팅방법"),
                                fieldWithPath("posts[].writer").type(JsonFieldType.STRING).description("작성자 닉네임"),
                                fieldWithPath("posts[].postSkills").type(JsonFieldType.ARRAY).description("게시물지정스킬목록"),
                                fieldWithPath("posts[].customSkills[].id").type(JsonFieldType.NUMBER).description("커스텀 스킬 식별자"),
                                fieldWithPath("posts[].customSkills[].name").type(JsonFieldType.STRING).description("커스텀 스킬 이름"),
                                fieldWithPath("posts[].postLikeStatus").type(JsonFieldType.BOOLEAN).description("찜하기여부 - 찜하면 true")
                        )
                ));

    }

    @Test
    @DisplayName("게시물 찜목록 조회 문서화")
    public void getLikePosts() throws Exception {
        List<PostResponse.GetPost> posts = new ArrayList<>();
        PostResponse.GetPost post1 = PostResponse.GetPost.build(post, true);
        PostResponse.GetPost post2 = PostResponse.GetPost.build(post, true);
        posts.add(post1);
        posts.add(post2);

        PostResponse.GetPosts response = PostResponse.GetPosts.build(posts);

        given(postService.getLikePosts(any())).willReturn(response);

        ResultActions results = mvc.perform(get("/api/posts/like", user)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post_likePosts",
                        responseFields(
                                fieldWithPath("posts").type(JsonFieldType.ARRAY).description("찜 목록"),
                                fieldWithPath("posts[].id").type(JsonFieldType.NUMBER).description("게시글식별자"),
                                fieldWithPath("posts[].title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("posts[].region").type(JsonFieldType.STRING).description("지역"),
                                fieldWithPath("posts[].postStatus").type(JsonFieldType.STRING).description("진행상태"),
                                fieldWithPath("posts[].kinds").type(JsonFieldType.STRING).description("글종류"),
                                fieldWithPath("posts[].meeting").type(JsonFieldType.STRING).description("미팅방법"),
                                fieldWithPath("posts[].writer").type(JsonFieldType.STRING).description("작성자 닉네임"),
                                fieldWithPath("posts[].postSkills").type(JsonFieldType.ARRAY).description("게시물지정스킬목록"),
                                fieldWithPath("posts[].customSkills[].id").type(JsonFieldType.NUMBER).description("커스텀 스킬 식별자"),
                                fieldWithPath("posts[].customSkills[].name").type(JsonFieldType.STRING).description("커스텀 스킬 이름"),
                                fieldWithPath("posts[].postLikeStatus").type(JsonFieldType.BOOLEAN).description("찜하기여부 - 찜하면 true")
                        )
                ));

    }

    @Test
    @DisplayName("게시물 찜 생성및삭제 문서화")
    public void updatePostLike() throws Exception {
        boolean postLikeStatus = true;
        PostResponse.GetPostLike response = PostResponse.GetPostLike.build(postLikeStatus);

        given(postService.updatePostLike(any(), any())).willReturn(response);

        ResultActions results = mvc.perform(RestDocumentationRequestBuilders
                .post("/api/post/{postId}/like", 1L));

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post_postLike",
                        pathParameters(
                                parameterWithName("postId").description("게시물 식별자")
                        ),
                        responseFields(
                                fieldWithPath("postLikeStatus").type(JsonFieldType.BOOLEAN).description("찜하기여부 - 찜하면 true")
                        )
                ));

    }

}

