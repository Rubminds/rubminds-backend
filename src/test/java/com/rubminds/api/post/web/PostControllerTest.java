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

@DisplayName("게시물 정보입력(생성) 문서화")
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
                .nickname("동그라미")
                .job("학생")
                .introduce("안녕하세요!")
                .provider(SignupProvider.RUBMINDS)
                .signupCheck(true)
                .avatar(Avatar.builder().url("profile url").build())
                .build();

        post1 = Post.builder()
                .id(1L)
                .title("테스트")
                .content("내용")
                .region("서울")
                .postStatus(PostStatus.RECRUIT)
                .kinds(Kinds.PROJECT)
                .headcount(3)
                .meeting(Meeting.BOTH)
                .writer(user)
                .postSkills(Collections.singletonList(PostSkill.builder().id(1L).skill(Skill.builder().id(1L).name("JAVA").build()).build()))
                .customSkills(Collections.singletonList(CustomSkill.builder().id(1L).name("java").build()))
                .team(Team.builder().id(1L).admin(user).build())
                .postFileList(Collections.singleton(PostFile.builder().id(1L).url("file url").complete(false).build()))
                .postFileList(Collections.singleton(PostFile.builder().id(1L).url("completefile url").complete(true).build()))
                .build();

        post1.setCreatedAt(LocalDateTime.of(2021,2,3,9,00));
        post2 = Post.builder()
                .id(2L)
                .title("테스트2")
                .content("내용2")
                .region("서울")
                .postStatus(PostStatus.RECRUIT)
                .kinds(Kinds.PROJECT)
                .headcount(3)
                .meeting(Meeting.BOTH)
                .writer(user)
                .postSkills(Collections.singletonList(PostSkill.builder().id(1L).skill(Skill.builder().id(1L).name("JAVA").build()).build()))
                .customSkills(Collections.singletonList(CustomSkill.builder().id(1L).name("react").build()))
                .team(Team.builder().id(1L).admin(user).build())
                .postFileList(Collections.singleton(PostFile.builder().id(1L).url("file url").build()))
                .build();
        postList.add(post1);
        postList.add(post2);

        completeFile = PostFile.builder().complete(true).url("completeFileUrl").build();
        postFile = PostFile.builder().complete(false).url("completeUrl").build();

        completeImage = PostFile.builder().complete(true).url("completeImageUrl").build();
        completeImages.add(completeImage);

        postFiles.add(postFile);
    }

    @Test
    @DisplayName("게시물 생성 문서화")
    public void create() throws Exception {
        PostRequest.Create request = PostRequest.Create.builder()
                .title("테스트")
                .content("내용")
                .region("서울")
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
                                partWithName("files").description("파일"),
                                partWithName("postInfo").description("게시물 정보 - JSON")
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
                .title("테스트")
                .content("내용")
                .region("서울")
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
                                parameterWithName("postId").description("게시물 식별자")
                        ),
                        requestParts(
                                partWithName("files").description("파일"),
                                partWithName("postInfo").description("게시물 정보 - JSON")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("게시글 식별자")
                        )
                ));
    }

    @Test
    @DisplayName("게시물 디테일 조회 문서화")
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
                                parameterWithName("postId").description("게시물 식별자")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("게시글식별자"),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                                fieldWithPath("kinds").type(JsonFieldType.STRING).description("게시물 종류"),
                                fieldWithPath("region").type(JsonFieldType.STRING).description("지역"),
                                fieldWithPath("postsStatus").type(JsonFieldType.STRING).description("진행상태"),
                                fieldWithPath("headcount").type(JsonFieldType.NUMBER).description("모집인원"),
                                fieldWithPath("meeting").type(JsonFieldType.STRING).description("미팅방법"),
                                fieldWithPath("createAt").type(JsonFieldType.STRING).description("작성 날짜"),
                                fieldWithPath("writer.nickname").type(JsonFieldType.STRING).description("작성자 닉네임"),
                                fieldWithPath("writer.avatar").type(JsonFieldType.STRING).description("작성자 프로필 url"),
                                fieldWithPath("writer.id").type(JsonFieldType.NUMBER).description("작성자 식별자"),
                                fieldWithPath("files[].url").type(JsonFieldType.STRING).description("파일"),
                                fieldWithPath("completeFile.url").type(JsonFieldType.STRING).description("완료게시글 파일"),
                                fieldWithPath("completeImages[].url").type(JsonFieldType.STRING).description("완료게시글 이미지").optional(),
                                fieldWithPath("postSkills[]").type(JsonFieldType.ARRAY).description("게시물 스킬"),
                                fieldWithPath("customSkills[]").type(JsonFieldType.ARRAY).description("커스텀스킬(직접입력한)"),
                                fieldWithPath("isLike").type(JsonFieldType.BOOLEAN).description("자신이 찜한 게시물이라면 true"),
                                fieldWithPath("teamId").type(JsonFieldType.NUMBER).description("팀 id"),
                                fieldWithPath("refLink").type(JsonFieldType.STRING).description("참조링크").optional(),
                                fieldWithPath("completeContent").type(JsonFieldType.STRING).description("완료게시글내용").optional()

                        )
                ));

    }

    @Test
    @DisplayName("게시물 목록 조회 문서화")
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
                                parameterWithName("page").description("조회할 페이지"),
                                parameterWithName("size").description("조회할 사이즈"),
                                parameterWithName("kinds").description("게시물 종류 (PROJECT,SCOUT,STUDY)"),
                                parameterWithName("status").description("게시물상태 (RECRUIT,FINISHED)"),
                                parameterWithName("skill").description("skill 식별자"),
                                parameterWithName("region").description("지역 조회"),
                                parameterWithName("keywords").description("직접 입력 키워드")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("content[].id").type(JsonFieldType.NUMBER).description("게시글식별자"),
                                fieldWithPath("content[].title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("content[].kinds").type(JsonFieldType.STRING).description("글종류"),
                                fieldWithPath("content[].region").type(JsonFieldType.STRING).description("지역"),
                                fieldWithPath("content[].status").type(JsonFieldType.STRING).description("글 상태"),
                                fieldWithPath("content[].skill[]").type(JsonFieldType.ARRAY).description("스킬 식별자"),
                                fieldWithPath("content[].customSkills[]").type(JsonFieldType.ARRAY).description("커스텀 스킬 식별자"),
                                fieldWithPath("content[].isLike").type(JsonFieldType.BOOLEAN).description("찜하기여부 - 찜하면 true"),
                                fieldWithPath("totalElements").description("전체 개수"),
                                fieldWithPath("last").description("마지막 페이지인지 식별"),
                                fieldWithPath("totalPages").description("전체 페이지")
                        )
                ));
    }

    @Test
    @DisplayName("게시물 찜목록 조회 문서화")
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
                                parameterWithName("page").description("조회할 페이지"),
                                parameterWithName("size").description("조회할 사이즈"),
                                parameterWithName("kinds").description("게시물 종류 (PROJECT,SCOUT,STUDY)")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("content[].id").type(JsonFieldType.NUMBER).description("게시글식별자"),
                                fieldWithPath("content[].title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("content[].kinds").type(JsonFieldType.STRING).description("글종류"),
                                fieldWithPath("content[].region").type(JsonFieldType.STRING).description("지역"),
                                fieldWithPath("content[].status").type(JsonFieldType.STRING).description("글 상태"),
                                fieldWithPath("content[].skill[]").type(JsonFieldType.ARRAY).description("커스텀 스킬 식별자"),
                                fieldWithPath("content[].isLike").type(JsonFieldType.BOOLEAN).description("찜하기여부 - 찜하면 true"),
                                fieldWithPath("totalElements").description("전체 개수"),
                                fieldWithPath("last").description("마지막 페이지인지 식별"),
                                fieldWithPath("totalPages").description("전체 페이지")
                        )
                ));

    }

    @Test
    @DisplayName("게시물 찜 생성및삭제 문서화")
    public void updatePostLike() throws Exception {
        ResultActions results = mvc.perform(RestDocumentationRequestBuilders
                .post("/api/post/{postId}/like", 1L));

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post_postLike",
                        pathParameters(
                                parameterWithName("postId").description("게시물 식별자")
                        )
                ));

    }

    @Test
    @DisplayName("완료게시글 작성")
    public void updatePostComplete() throws Exception {
        PostRequest.CreateCompletePost request = PostRequest.CreateCompletePost.builder()
                .refLink("http://rubmind.com")
                .completeContent("완성했습니다.")
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
                                parameterWithName("postId").description("게시물 식별자")
                        ),
                        requestParts(
                                partWithName("completeFiles").description("완료게시글 첨부파일"),
                                partWithName("completeInfo").description("완료게시물 정보 - JSON")

                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("게시글 식별자")
                        )
                ));
    }


    @Test
    @DisplayName("게시물 상태 변화시키기")
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
                .andDo(document("post_chageStatus",
                        pathParameters(
                                parameterWithName("postId").description("게시물 식별자")
                        ),
                        requestFields(
                                fieldWithPath("postStatus").type(JsonFieldType.STRING).description("상태변화시킬값(RECRUIT, WORKING, RANKING, FINISHED),")

                        ),
                        relaxedResponseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("게시글식별자")

                        )
                ));

    }
    @Test
    @DisplayName("유저 게시물 목록 조회 문서화")
    public void getPostsByStatus() throws Exception {
        Page<Post> postPage = new PageImpl<>(postList, PageRequest.of(1, 5), postList.size());
        Page<PostResponse.GetListByStatus> response = postPage.map(post1 -> PostResponse.GetListByStatus.build(post1));

        given(postService.getListByStatus(any(), any(), any())).willReturn(response);

        ResultActions results = mvc.perform(RestDocumentationRequestBuilders.get("/api/user/{userId}/posts", 1L)
                .param("status", "RECRUIT")
                .param("page", "1")
                .param("size", "5")
        );

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("post_postsByStatus",
                        pathParameters(
                            parameterWithName("userId").description("유저 식별자")
                        ),
                        requestParameters(
                                parameterWithName("page").description("조회할 페이지"),
                                parameterWithName("size").description("조회할 사이즈"),
                                parameterWithName("status").description("게시물 상태 (RECRUIT,WORKING,RANKING,FINISHED)")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("content[].id").type(JsonFieldType.NUMBER).description("게시글식별자"),
                                fieldWithPath("content[].writer").type(JsonFieldType.STRING).description("작성자(닉네임)"),
                                fieldWithPath("content[].title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("content[].kinds").type(JsonFieldType.STRING).description("글 종류"),
                                fieldWithPath("content[].region").type(JsonFieldType.STRING).description("지역"),
                                fieldWithPath("content[].status").type(JsonFieldType.STRING).description("글 상태"),
                                fieldWithPath("content[].postSkills[]").type(JsonFieldType.ARRAY).description("포스트 스킬 이름 목록"),
                                fieldWithPath("content[].customSkills[]").type(JsonFieldType.ARRAY).description("커스텀 스킬 이름 목록"),
                                fieldWithPath("totalElements").description("전체 개수"),
                                fieldWithPath("last").description("마지막 페이지인지 식별"),
                                fieldWithPath("totalPages").description("전체 페이지")
                        )
                ));

    }
}

