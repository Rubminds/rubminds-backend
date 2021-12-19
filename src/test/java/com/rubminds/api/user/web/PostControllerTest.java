package com.rubminds.api.user.web;

import com.rubminds.MvcTest;
import com.rubminds.api.post.domain.Post;
import com.rubminds.api.post.domain.PostEnumClass.Kinds;
import com.rubminds.api.post.domain.PostEnumClass.Meeting;
import com.rubminds.api.post.domain.PostEnumClass.PostStatus;
import com.rubminds.api.post.domain.PostEnumClass.Region;
import com.rubminds.api.post.domain.repository.PostRepository;
import com.rubminds.api.post.dto.PostLikeRequest;
import com.rubminds.api.post.dto.PostRequest;
import com.rubminds.api.post.dto.PostResponse;
import com.rubminds.api.post.service.PostService;
import com.rubminds.api.post.web.PostController;
import com.rubminds.api.skill.domain.CostomSkill;
import com.rubminds.api.skill.domain.PostSkill;
import com.rubminds.api.skill.domain.Skill;
import com.rubminds.api.skill.domain.repository.PostSkillRepository;
import com.rubminds.api.skill.domain.repository.SkillRepository;
import com.rubminds.api.skill.dto.CostomSkillRequest;
import com.rubminds.api.skill.dto.PostSkillRequest;
import com.rubminds.api.user.domain.SignupProvider;
import com.rubminds.api.user.domain.User;

import com.rubminds.api.user.dto.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.security.core.parameters.P;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
                .postSkillIds(List.of(1L,2L))
                .costomSkills(List.of("firebase","unity"))
                .build();

        PostResponse.OnlyId response = PostResponse.OnlyId.build(post);
        given(postService.create(any(), any())).willReturn(response);

        ResultActions results = mvc.perform(post("/api/post")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .characterEncoding("UTF-8")
        );

        results.andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("post_create",
                        requestFields(
                                fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                                fieldWithPath("region").type(JsonFieldType.STRING).description("지역"),
                                fieldWithPath("postsStatus").type(JsonFieldType.STRING).description("진행상태"),
                                fieldWithPath("kinds").type(JsonFieldType.STRING).description("글종류"),
                                fieldWithPath("headcount").type(JsonFieldType.NUMBER).description("모집인원"),
                                fieldWithPath("meeting").type(JsonFieldType.STRING).description("미팅방법"),
                                fieldWithPath("writer").type(JsonFieldType.STRING).description("작성자 닉네임"),
                                fieldWithPath("postSkillIds").type(JsonFieldType.ARRAY).description("게시물지정스킬목록"),
                                fieldWithPath("costomSkills").type(JsonFieldType.ARRAY).description("키타스킬목록지정")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("게시글 식별자")
                        )
                ));
    }

    @Test
    @DisplayName("게시물 수정 문서화")
    public void updatePost() throws Exception {
        Long id = 1L;
        PostRequest.Create request = PostRequest.Create.builder()
                .writer(user.getNickname())
                .title("테스트")
                .content("내용")
                .region(Region.서울)
                .postsStatus(PostStatus.GATHERING)
                .kinds(Kinds.PROJECT)
                .headcount(3)
                .meeting(Meeting.BOTH)
                .postSkillIds(List.of(1L,2L))
                .costomSkills(List.of("firebase","unity"))
                .build();

        PostResponse.OnlyId response = PostResponse.OnlyId.build(post);
        given(postService.update(any(), any())).willReturn(response);

        ResultActions results = mvc.perform(put("/api/post/{id}",id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .characterEncoding("UTF-8")
                .param("id","게시글 id")
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
                                fieldWithPath("writer").type(JsonFieldType.STRING).description("작성자 닉네임"),
                                fieldWithPath("postSkillIds").type(JsonFieldType.ARRAY).description("게시물지정스킬목록"),
                                fieldWithPath("costomSkills").type(JsonFieldType.ARRAY).description("키타스킬목록지정")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("게시글 식별자")
                        )
                ));
    }

    @Test
    @DisplayName("게시물 삭제 문서화")
    public void deletePost() throws Exception {
        Long id = 1L;
        PostRequest.Create request = PostRequest.Create.builder()
                .writer(user.getNickname())
                .title("테스트")
                .content("내용")
                .region(Region.서울)
                .postsStatus(PostStatus.GATHERING)
                .kinds(Kinds.PROJECT)
                .headcount(3)
                .meeting(Meeting.BOTH)
                .postSkillIds(List.of(1L,2L))
                .costomSkills(List.of("firebase","unity"))
                .build();


        ResultActions results = mvc.perform(put("/api/post/{id}",id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .characterEncoding("UTF-8")
                .param("id","게시글 id")
        );

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post_delete"));
    }


    @Test
    @DisplayName("게시물 디테일 조회 문서화")
    public void detailPost() throws Exception {
        Long id = 1L;
        PostRequest.Create request = PostRequest.Create.builder()
                .writer(user.getNickname())
                .title("테스트")
                .content("내용")
                .region(Region.서울)
                .postsStatus(PostStatus.GATHERING)
                .kinds(Kinds.PROJECT)
                .headcount(3)
                .meeting(Meeting.BOTH)
                .postSkillIds(List.of(1L,2L))
                .costomSkills(List.of("firebase","unity"))
                .build();

        Skill skill = Skill.builder().id(1L).name("Spring").build();
        Skill skill1 = Skill.builder().id(2L).name("JavaScript").build();

        PostSkill postSkill = PostSkill.builder().post(post).skill(skill).id(1l).build();
        PostSkill postSkill1 = PostSkill.builder().post(post).skill(skill1).id(1l).build();
        List<PostSkill> postSkillRequestList = new ArrayList<>();
        postSkillRequestList.add(postSkill);
        postSkillRequestList.add(postSkill1);


        CostomSkill costomSkill = CostomSkill.builder().id(1l).post(post).name("javascript").build();
        CostomSkill costomSkill1 = CostomSkill.builder().id(2l).post(post).name("Spring").build();
        List<CostomSkill> costomSkills = new ArrayList<>();
        costomSkills.add(costomSkill);
        costomSkills.add(costomSkill1);

        boolean postLikeStatus = false;

        PostResponse.Info response = PostResponse.Info.build(post,postSkillRequestList,costomSkills, postLikeStatus);
        given(postService.getPost(any(),any())).willReturn(response);
        ResultActions results = mvc.perform(get("/api/post/{id}",id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .characterEncoding("UTF-8")
                .param("id","게시글 id")
        );

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post_detail",

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
                                fieldWithPath("postSkills").type(JsonFieldType.ARRAY).description("게시물지정스킬목록"),
                                fieldWithPath("customSkills").type(JsonFieldType.ARRAY).description("키타스킬목록지정"),
                                fieldWithPath("postSkills.[].postSkillId").type(JsonFieldType.NUMBER).description("게시물지정스킬목록 식별자"),
                                fieldWithPath("postSkills.[].name").type(JsonFieldType.STRING).description("게시물지정스킬 이름"),
                                fieldWithPath("customSkills.[].costomSkillId").type(JsonFieldType.NUMBER).description("게시물지정스킬목록 식별자"),
                                fieldWithPath("customSkills.[].name").type(JsonFieldType.STRING).description("게시물지정스킬 이름"),
                                fieldWithPath("postLikeStatus").type(JsonFieldType.BOOLEAN).description("찜하기여부 - 찜하면 true")

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

        ResultActions results = mvc.perform(get("/api/post/getPosts", user)
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
                                fieldWithPath("posts[].customSkills").type(JsonFieldType.ARRAY).description("키타스킬목록지정"),
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

        ResultActions results = mvc.perform(get("/api/post/getLikePosts", user)
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
                                fieldWithPath("posts[].customSkills").type(JsonFieldType.ARRAY).description("키타스킬목록지정"),
                                fieldWithPath("posts[].postLikeStatus").type(JsonFieldType.BOOLEAN).description("찜하기여부 - 찜하면 true")
                        )
                ));

    }

    @Test
    @DisplayName("게시물 찜 생성및삭제 문서화")
    public void updatePostLike() throws Exception {
        PostLikeRequest.Update request = PostLikeRequest.Update.builder().postId(1L).build();
        boolean postLikeStatus = true;
        PostResponse.GetPostLike response = PostResponse.GetPostLike.build(postLikeStatus);

        given(postService.updatePostLike(any(),any())).willReturn(response);

        ResultActions results = mvc.perform(post("/api/post/postLike", user)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .characterEncoding("UTF-8")
        );

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post_postLike",
                        requestFields(
                                fieldWithPath("postId").type(JsonFieldType.NUMBER).description("게시글 식별자")
                        ),
                        responseFields(
                                fieldWithPath("postLikeStatus").type(JsonFieldType.BOOLEAN).description("찜하기여부 - 찜하면 true")
                        )
                ));

    }
}

