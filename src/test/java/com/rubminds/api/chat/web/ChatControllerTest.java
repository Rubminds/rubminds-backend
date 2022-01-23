package com.rubminds.api.chat.web;

import com.rubminds.MvcTest;
import com.rubminds.api.chat.domain.Chat;
import com.rubminds.api.file.domain.Avatar;
import com.rubminds.api.chat.dto.ChatRequest;
import com.rubminds.api.chat.dto.ChatResponse;
import com.rubminds.api.chat.service.ChatService;
import com.rubminds.api.post.domain.*;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import javax.print.DocFlavor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("쪽지 API 문서화")
@WebMvcTest(ChatController.class)
public class ChatControllerTest extends MvcTest {

    @MockBean
    private ChatService chatService;
    private Chat chat;
    private List<Chat> chats  = new ArrayList<>() ;
    private List<ChatResponse.GetPostList> posts  = new ArrayList<>() ;
    private Post post1;
    private ChatResponse.GetPostList post2;
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

        chat = chat.builder()
                .id(1L)
                .content("채팅내용")
                .sender(user)
                .post(post1)
                .build();

        post2 = post2.builder()
                .postId(1L)
                .postTitle("제목")
                .build();

        chats.add(chat);
        posts.add(post2);


    }



    @Test
    @DisplayName("쪽지 보내기 문서화")
    public void sendMessage() throws Exception {
        ChatRequest.Create request = ChatRequest.Create.builder().postId(1L).content("쪽지내용").build();
        ChatResponse.OnlyId response = ChatResponse.OnlyId.build(chat);

        given(chatService.create(any(),any())).willReturn(response);

        ResultActions results = mvc.perform(post("/api/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .characterEncoding("UTF-8")
        );


        results.andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("send_message",
                        requestFields(
                                fieldWithPath("postId").type(JsonFieldType.NUMBER).description("게시물 ID"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("내용")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("쪽지 식별자")
                        )
                ));
    }

    @Test
    @DisplayName("쪽지내역 보기")
    public void getChats() throws Exception {
        Page<Chat> chatPage = new PageImpl<>(chats, PageRequest.of(1, 5), chats.size());
        ChatResponse.GetList response = ChatResponse.GetList.build(post1, chatPage);
        given(chatService.getChatList( any(),any(), any())).willReturn(response);

        ResultActions results = mvc.perform(RestDocumentationRequestBuilders
                .get("/api/chat/{postId}",1L)
                .param("page", "1")
                .param("size", "10"));

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("chat_list", requestParameters(
                                parameterWithName("page").description("조회할 페이지"),
                                parameterWithName("size").description("조회할 사이즈")
                        ),
                        pathParameters(
                                parameterWithName("postId").description("게시물 식별자")
                        ),
                        responseFields(
                                fieldWithPath("postId").type(JsonFieldType.NUMBER).description("게시글 식별자"),
                                fieldWithPath("postTitle").type(JsonFieldType.STRING).description("게시글 제목"),
                                fieldWithPath("writerId").type(JsonFieldType.NUMBER).description("게시글 작성자 식별자"),
                                fieldWithPath("chats[].id").type(JsonFieldType.NUMBER).description("쪽지 식별자"),
                                fieldWithPath("chats[].senderId").type(JsonFieldType.NUMBER).description("보낸이 식별자"),
                                fieldWithPath("chats[].senderNickname").type(JsonFieldType.STRING).description("보낸이 닉네임"),
                                fieldWithPath("chats[].avatar").type(JsonFieldType.STRING).description("보낸이 프로필").optional(),
                                fieldWithPath("chats[].content").type(JsonFieldType.STRING).description("보낸 쪽지 내용").optional(),
                                fieldWithPath("chats[].createAt").type(JsonFieldType.STRING).description("보낸 시간").optional()
                        )
                ));
    }

    @Test
    @DisplayName("쪽지방(게시물방) 보기")
    public void getPosts() throws Exception {
        Page<ChatResponse.GetPostList> postlist = new PageImpl<>(posts, PageRequest.of(1, 5), posts.size());
        given(chatService.getPostList(any(), any(), any())).willReturn(postlist);

        ResultActions results = mvc.perform(RestDocumentationRequestBuilders
                .get("/api/chat")
                .param("kinds", "PROJECT")
                .param("page", "1")
                .param("size", "10"));

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("chat_post_list", requestParameters(
                        parameterWithName("page").description("조회할 페이지"),
                        parameterWithName("size").description("조회할 사이즈"),
                        parameterWithName("kinds").description("게시글 종류")
                        ),

                        relaxedResponseFields(
                                fieldWithPath("content[].postId").type(JsonFieldType.NUMBER).description("게시글 식별자"),
                                fieldWithPath("content[].postTitle").type(JsonFieldType.STRING).description("게시글 제목")
                        )
                ));
    }



}
