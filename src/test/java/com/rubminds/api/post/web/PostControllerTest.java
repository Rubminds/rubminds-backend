package com.rubminds.api.post.web;

import com.rubminds.MvcTest;
import com.rubminds.api.file.domain.Avatar;
import com.rubminds.api.post.domain.*;
import com.rubminds.api.post.dto.PostRequest;
import com.rubminds.api.post.dto.PostResponse;
import com.rubminds.api.post.service.PostService;
import com.rubminds.api.skill.domain.CustomSkill;
import com.rubminds.api.skill.domain.Skill;
import com.rubminds.api.team.domain.Team;
import com.rubminds.api.user.domain.SignupProvider;
import com.rubminds.api.user.domain.User;
import com.rubminds.api.user.security.userdetails.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.String.format;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.fileUpload;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("????????? ????????????(??????) ?????????")
@WebMvcTest(PostController.class)
public class PostControllerTest extends MvcTest {

    @MockBean
    private PostService postService;
    private User user;
    private Post post1;
    private Post post2;
    private PostFile postFile;
    private PostFile completeFile;
    private PostFile completeImage;
    private List<PostFile> postFiles = new ArrayList<>();
    private List<PostFile> completeImages = new ArrayList<>();
    private List<Post> postList = new ArrayList<>();

    @BeforeEach
    public void setup() {
        user = User.builder()
                .id(1L)
                .oauthId("1")
                .nickname("????????????")
                .job("??????")
                .introduce("???????????????!")
                .provider(SignupProvider.RUBMINDS)
                .signupCheck(true)
                .avatar(Avatar.builder().url("profile url").build())
                .build();

        post1 = Post.builder()
                .id(1L)
                .title("?????????")
                .content("??????")
                .region("??????")
                .postStatus(PostStatus.RECRUIT)
                .kinds(Kinds.PROJECT)
                .headcount(3)
                .meeting(Meeting.BOTH)
                .writer(user)
                .postSkills(Collections.singletonList(PostSkill.builder().id(1L).skill(Skill.builder().id(1L).name("JAVA").url("JavaImgUrl").build()).build()))
                .customSkills(Collections.singletonList(CustomSkill.builder().id(1L).name("java").build()))
                .team(Team.builder().id(1L).admin(user).build())
                .postFileList(Collections.singleton(PostFile.builder().id(1L).originalName("file name").url("file url").complete(false).build()))
                .postFileList(Collections.singleton(PostFile.builder().id(1L).originalName("completefile name").url("completefile url").complete(true).build()))
                .build();

        post1.setCreatedAt(LocalDateTime.of(2021,2,3,9,00));
        post2 = Post.builder()
                .id(2L)
                .title("?????????2")
                .content("??????2")
                .region("??????")
                .postStatus(PostStatus.RECRUIT)
                .kinds(Kinds.PROJECT)
                .headcount(3)
                .meeting(Meeting.BOTH)
                .writer(user)
                .postSkills(Collections.singletonList(PostSkill.builder().id(1L).skill(Skill.builder().id(1L).name("JAVA").url("JavaImgUrl").build()).build()))
                .customSkills(Collections.singletonList(CustomSkill.builder().id(1L).name("react").build()))
                .team(Team.builder().id(1L).admin(user).build())
                .postFileList(Collections.singleton(PostFile.builder().id(1L).originalName("file name").url("file url").build()))
                .build();
        postList.add(post1);
        postList.add(post2);

        completeFile = PostFile.builder().complete(true).originalName("completeFile name").url("completeFileUrl").build();
        postFile = PostFile.builder().complete(false).originalName("postFile name").url("postFile url").build();

        completeImage = PostFile.builder().complete(true).originalName("completeImageName").url("completeImageUrl").build();
        completeImages.add(completeImage);

        postFiles.add(postFile);
    }

    @Test
    @DisplayName("????????? ?????? ?????????")
    public void create() throws Exception {
        PostRequest.Create request = PostRequest.Create.builder()
                .title("?????????")
                .content("??????")
                .region("??????")
                .kinds(Kinds.PROJECT)
                .headcount(3)
                .meeting(Meeting.BOTH)
                .skillIds(List.of(1L, 2L))
                .customSkillName(List.of("firebase", "unity"))
                .build();
        String content = objectMapper.writeValueAsString(request);
        MockMultipartFile postInfo = new MockMultipartFile("postInfo", "jsondata", "application/json", content.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ClassPathResource("dummy/image/white.jpeg").getInputStream();
        MockMultipartFile files = new MockMultipartFile("files", "white.jpeg", "image/jpeg", inputStream.readAllBytes());

        PostResponse.OnlyId response = PostResponse.OnlyId.build(post1);
        given(postService.create(any(), any(), any())).willReturn(response);

        ResultActions results = mvc.perform(multipart("/api/post")
                .file(postInfo)
                .file(files)
                .contentType(MediaType.MULTIPART_MIXED)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        results.andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("post_create",
                        requestParts(
                                partWithName("files").description("??????"),
                                partWithName("postInfo").description("????????? ?????? - JSON")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("????????? ?????????")
                        )
                ));
    }

    @Test
    @DisplayName("????????? ?????? ?????????")
    public void updatePost() throws Exception {
        PostRequest.Create request = PostRequest.Create.builder()
                .title("?????????")
                .content("??????")
                .region("??????")
                .kinds(Kinds.PROJECT)
                .headcount(3)
                .meeting(Meeting.BOTH)
                .skillIds(List.of(1L, 2L))
                .customSkillName(List.of("firebase", "unity"))
                .build();
        String content = objectMapper.writeValueAsString(request);
        MockMultipartFile postInfo = new MockMultipartFile("postInfo", "jsondata", "application/json", content.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ClassPathResource("dummy/image/white.jpeg").getInputStream();
        MockMultipartFile files = new MockMultipartFile("files", "white.jpeg", "image/jpeg", inputStream.readAllBytes());

        PostResponse.OnlyId response = PostResponse.OnlyId.build(post1);
        given(postService.update(any(), any(), any())).willReturn(response);

        ResultActions results = mvc.perform(fileUpload(format("/api/post/{postId}/update"), 1L)
                .file(postInfo)
                .file(files)
                .contentType(MediaType.MULTIPART_MIXED)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post_update",
                        pathParameters(
                                parameterWithName("postId").description("????????? ?????????")
                        ),
                        requestParts(
                                partWithName("files").description("??????"),
                                partWithName("postInfo").description("????????? ?????? - JSON")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("????????? ?????????")
                        )
                ));
    }

    @Test
    @DisplayName("????????? ????????? ?????? ?????????")
    public void detailPost() throws Exception {
        CustomUserDetails customUserDetails = CustomUserDetails.create(user);

        PostResponse.Info response = PostResponse.Info.build(post1, customUserDetails, postFiles, completeFile, completeImages);

        given(postService.getOne(any(), any())).willReturn(response);

        ResultActions results = mvc.perform(RestDocumentationRequestBuilders
                .get("/api/post/{postId}", 1L));

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post_detail",
                        pathParameters(
                                parameterWithName("postId").description("????????? ?????????")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("??????????????????"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("??????"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("??????"),
                                fieldWithPath("kinds").type(JsonFieldType.STRING).description("????????? ??????"),
                                fieldWithPath("region").type(JsonFieldType.STRING).description("??????"),
                                fieldWithPath("postsStatus").type(JsonFieldType.STRING).description("????????????"),
                                fieldWithPath("headcount").type(JsonFieldType.NUMBER).description("????????????"),
                                fieldWithPath("meeting").type(JsonFieldType.STRING).description("????????????"),
                                fieldWithPath("createAt").type(JsonFieldType.STRING).description("?????? ??????"),
                                fieldWithPath("writer.nickname").type(JsonFieldType.STRING).description("????????? ?????????"),
                                fieldWithPath("writer.avatar").type(JsonFieldType.STRING).description("????????? ????????? url"),
                                fieldWithPath("writer.id").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                                fieldWithPath("files[].fileName").type(JsonFieldType.STRING).description("????????? ?????? ???").optional(),
                                fieldWithPath("files[].url").type(JsonFieldType.STRING).description("????????? ?????? Url").optional(),
                                fieldWithPath("completeFile.fileName").type(JsonFieldType.STRING).description("??????????????? ?????? ???"),
                                fieldWithPath("completeFile.url").type(JsonFieldType.STRING).description("??????????????? ?????? Url"),
                                fieldWithPath("completeImages[].fileName").type(JsonFieldType.STRING).description("??????????????? ????????? ???").optional(),
                                fieldWithPath("completeImages[].url").type(JsonFieldType.STRING).description("??????????????? ????????? Url").optional(),
                                fieldWithPath("postSkills[]").type(JsonFieldType.ARRAY).description("????????? ??????"),
                                fieldWithPath("customSkills[]").type(JsonFieldType.ARRAY).description("???????????????(???????????????)"),
                                fieldWithPath("isLike").type(JsonFieldType.BOOLEAN).description("????????? ?????? ?????????????????? true"),
                                fieldWithPath("teamId").type(JsonFieldType.NUMBER).description("??? id"),
                                fieldWithPath("refLink").type(JsonFieldType.STRING).description("????????????").optional(),
                                fieldWithPath("completeContent").type(JsonFieldType.STRING).description("?????????????????????").optional()

                        )
                ));

    }

    @Test
    @DisplayName("????????? ?????? ?????? ?????????")
    public void getPosts() throws Exception {
        CustomUserDetails customUserDetails = CustomUserDetails.create(user);
        Page<Post> postPage = new PageImpl<>(postList, PageRequest.of(1, 5), postList.size());
        Page<PostResponse.GetList> response = postPage.map(post1 -> PostResponse.GetList.build(post1, customUserDetails));

        given(postService.getList(any(), any(), any(), any(), any(), any(), any())).willReturn(response);

        ResultActions results = mvc.perform(get("/api/posts")
                .param("page", "1")
                .param("size", "10")
                .param("region", "Seoul")
                .param("kinds", "PROJECT")
                .param("keywords", "firebase")
                .param("keywords", "jpa")
                .param("skill", "1")
                .param("skill", "2")
                .param("status", "RECRUIT")
        );

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post_list",
                        requestParameters(
                                parameterWithName("page").description("????????? ?????????"),
                                parameterWithName("size").description("????????? ?????????"),
                                parameterWithName("kinds").description("????????? ?????? (PROJECT,SCOUT,STUDY)"),
                                parameterWithName("status").description("??????????????? (RECRUIT,FINISHED)"),
                                parameterWithName("skill").description("skill ?????????"),
                                parameterWithName("region").description("?????? ??????"),
                                parameterWithName("keywords").description("?????? ?????? ?????????")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("content[].id").type(JsonFieldType.NUMBER).description("??????????????????"),
                                fieldWithPath("content[].title").type(JsonFieldType.STRING).description("??????"),
                                fieldWithPath("content[].kinds").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("content[].region").type(JsonFieldType.STRING).description("??????"),
                                fieldWithPath("content[].status").type(JsonFieldType.STRING).description("??? ??????"),
                                fieldWithPath("content[].skill[]").type(JsonFieldType.ARRAY).description("?????? ?????????"),
                                fieldWithPath("content[].customSkills[]").type(JsonFieldType.ARRAY).description("????????? ?????? ?????????"),
                                fieldWithPath("content[].isLike").type(JsonFieldType.BOOLEAN).description("??????????????? - ????????? true"),
                                fieldWithPath("totalElements").description("?????? ??????"),
                                fieldWithPath("last").description("????????? ??????????????? ??????"),
                                fieldWithPath("totalPages").description("?????? ?????????")
                        )
                ));
    }

    @Test
    @DisplayName("????????? ????????? ?????? ?????????")
    public void getLikePosts() throws Exception {
        CustomUserDetails customUserDetails = CustomUserDetails.create(user);
        Page<Post> postPage = new PageImpl<>(postList, PageRequest.of(1, 5), postList.size());
        Page<PostResponse.GetList> response = postPage.map(post1 -> PostResponse.GetList.build(post1, customUserDetails));

        given(postService.getLikePosts(any(), any(), any())).willReturn(response);

        ResultActions results = mvc.perform(get("/api/posts/like", user)
                .param("page", "1")
                .param("size", "10")
                .param("kinds", "PROJECT")
        );

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post_likePosts",
                        requestParameters(
                                parameterWithName("page").description("????????? ?????????"),
                                parameterWithName("size").description("????????? ?????????"),
                                parameterWithName("kinds").description("????????? ?????? (PROJECT,SCOUT,STUDY)")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("content[].id").type(JsonFieldType.NUMBER).description("??????????????????"),
                                fieldWithPath("content[].title").type(JsonFieldType.STRING).description("??????"),
                                fieldWithPath("content[].kinds").type(JsonFieldType.STRING).description("?????????"),
                                fieldWithPath("content[].region").type(JsonFieldType.STRING).description("??????"),
                                fieldWithPath("content[].status").type(JsonFieldType.STRING).description("??? ??????"),
                                fieldWithPath("content[].skill[]").type(JsonFieldType.ARRAY).description("????????? ?????? ?????????"),
                                fieldWithPath("content[].isLike").type(JsonFieldType.BOOLEAN).description("??????????????? - ????????? true"),
                                fieldWithPath("totalElements").description("?????? ??????"),
                                fieldWithPath("last").description("????????? ??????????????? ??????"),
                                fieldWithPath("totalPages").description("?????? ?????????")
                        )
                ));

    }

    @Test
    @DisplayName("????????? ??? ??????????????? ?????????")
    public void updatePostLike() throws Exception {
        ResultActions results = mvc.perform(RestDocumentationRequestBuilders
                .post("/api/post/{postId}/like", 1L));

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post_postLike",
                        pathParameters(
                                parameterWithName("postId").description("????????? ?????????")
                        )
                ));

    }

    @Test
    @DisplayName("??????????????? ??????")
    public void updatePostComplete() throws Exception {
        PostRequest.CreateCompletePost request = PostRequest.CreateCompletePost.builder()
                .refLink("http://rubmind.com")
                .completeContent("??????????????????.")
                .build();

        String content = objectMapper.writeValueAsString(request);
        MockMultipartFile completeInfo = new MockMultipartFile("completeInfo", "jsondata", "application/json", content.getBytes(StandardCharsets.UTF_8));
        InputStream inputStream = new ClassPathResource("dummy/image/white.jpeg").getInputStream();
        MockMultipartFile files = new MockMultipartFile("completeFiles", "white.jpeg", "image/jpeg", inputStream.readAllBytes());

        PostResponse.OnlyId response = PostResponse.OnlyId.build(post1);
        given(postService.updateCompletePost(any(), any(),any(), any())).willReturn(response);

        ResultActions results = mvc.perform(fileUpload(format("/api/post/{postId}/complete"), 1L)
                .file(completeInfo)
                .file(files)
                .contentType(MediaType.MULTIPART_MIXED)
                .accept(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
        );

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post_complete",
                        pathParameters(
                                parameterWithName("postId").description("????????? ?????????")
                        ),
                        requestParts(
                                partWithName("completeFiles").description("??????????????? ????????????"),
                                partWithName("completeInfo").description("??????????????? ?????? - JSON")

                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("????????? ?????????")
                        )
                ));
    }


    @Test
    @DisplayName("????????? ?????? ???????????????")
    public void changePostStatus() throws Exception {
        PostRequest.ChangeStatus request = PostRequest.ChangeStatus.builder().postStatus(PostStatus.WORKING).build();

        PostResponse.OnlyId response = PostResponse.OnlyId.build(post1);
        given(postService.changeStatus(any(), any(), any())).willReturn(response);

        ResultActions results = mvc.perform(RestDocumentationRequestBuilders
                .put("/api/post/{postId}/changeStatus", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .characterEncoding("UTF-8"));

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post_changeStatus",
                        pathParameters(
                                parameterWithName("postId").description("????????? ?????????")
                        ),
                        requestFields(
                                fieldWithPath("postStatus").type(JsonFieldType.STRING).description("?????????????????????(RECRUIT, WORKING, RANKING, FINISHED),")

                        ),
                        relaxedResponseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("??????????????????")

                        )
                ));

    }
    @Test
    @DisplayName("?????? ????????? ?????? ?????? ?????????")
    public void getPostsByStatus() throws Exception {
        CustomUserDetails customUserDetails = CustomUserDetails.create(user);
        Page<Post> postPage = new PageImpl<>(postList, PageRequest.of(1, 5), postList.size());
        PostResponse.GetListByStatus response = PostResponse.GetListByStatus.build(user, postPage, customUserDetails);

        given(postService.getListByStatus(any(), any(), any(), any())).willReturn(response);

        ResultActions results = mvc.perform(RestDocumentationRequestBuilders.get("/api/user/{userId}/posts", 1L)
                .param("status", "RECRUIT")
                .param("page", "1")
                .param("size", "5")
        );

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post_postsByStatus",
                        pathParameters(
                            parameterWithName("userId").description("?????? ?????????")
                        ),
                        requestParameters(
                                parameterWithName("page").description("????????? ?????????"),
                                parameterWithName("size").description("????????? ?????????"),
                                parameterWithName("status").description("????????? ?????? (RECRUIT,WORKING,RANKING,FINISHED)")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("?????? ?????? ?????????"),
                                fieldWithPath("posts.content[].id").type(JsonFieldType.NUMBER).description("??????????????????"),
                                fieldWithPath("posts.content[].title").type(JsonFieldType.STRING).description("??????"),
                                fieldWithPath("posts.content[].kinds").type(JsonFieldType.STRING).description("??? ??????"),
                                fieldWithPath("posts.content[].region").type(JsonFieldType.STRING).description("??????"),
                                fieldWithPath("posts.content[].status").type(JsonFieldType.STRING).description("??? ??????"),
                                fieldWithPath("posts.content[].skill[]").type(JsonFieldType.ARRAY).description("????????? ?????? ?????? ??????"),
                                fieldWithPath("posts.content[].customSkills[]").type(JsonFieldType.ARRAY).description("????????? ?????? ?????? ??????"),
                                fieldWithPath("posts.content[].isLike").type(JsonFieldType.BOOLEAN).description("??????????????? - ????????? true"),
                                fieldWithPath("posts.totalElements").description("?????? ??????"),
                                fieldWithPath("posts.last").description("????????? ??????????????? ??????"),
                                fieldWithPath("posts.totalPages").description("?????? ?????????")
                        )
                ));

    }

    @Test
    @DisplayName("????????? ????????? ????????? ????????????")
    public void getPostsTitle() throws Exception {
        List<PostResponse.GetTitleList> responseList = new ArrayList<>();
        CustomUserDetails customUserDetails = CustomUserDetails.create(user);
        PostResponse.GetTitleList response = PostResponse.GetTitleList.build(post1);
        responseList.add(response);

        given(postService.getTitleList(any())).willReturn(responseList);

        ResultActions results = mvc.perform(RestDocumentationRequestBuilders.get("/api/post/user")
        );

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post_getTitleList",
                        relaxedResponseFields(
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("????????? ?????????"),
                                fieldWithPath("[].title").type(JsonFieldType.STRING).description("??????"),
                                fieldWithPath("[].status").type(JsonFieldType.STRING).description("????????? ??????"),
                                fieldWithPath("[].kinds").type(JsonFieldType.STRING).description("??????")
                        )
                ));

    }

    @Test
    @DisplayName("????????? ?????? ?????????")
    public void deletePost() throws Exception {
        PostResponse.OnlyId response = PostResponse.OnlyId.build(post1);

        given(postService.delete(any(), any())).willReturn(response);

        ResultActions results = mvc.perform(RestDocumentationRequestBuilders
                .delete("/api/post/{postId}", 1L));

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post_delete",
                        pathParameters(
                                parameterWithName("postId").description("????????? ?????????")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("????????? ?????????")
                        )
                ));

    }
}

