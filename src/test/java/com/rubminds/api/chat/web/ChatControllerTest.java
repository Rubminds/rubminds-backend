package com.rubminds.api.chat.web;

import com.rubminds.MvcTest;
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
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("쪽지 API 문서화")
@WebMvcTest(ChatController.class)
public class ChatControllerTest extends MvcTest {

    @MockBean
    private ChatService chatService;
    private Message message;
    private Post post1;
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

        message = Message.builder()
                .id(1L)
                .content("채팅내용")
                .read(false)
                .receiverId(2L)
                .senderId(1L)
                .post(post1)
                .build();
    }



    @Test
    @DisplayName("쪽지 보내기 문서화")
    public void sendMessage() throws Exception {
        ChatRequest.Create request = ChatRequest.Create.builder().receiverId(2L).content("쪽지내용").postId(1L).build();
        ChatResponse.OnlyId response = ChatResponse.OnlyId.build(message);

        given(chatService.create(any(),any())).willReturn(response);

        ResultActions results = mvc.perform(post("/api/message")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .characterEncoding("UTF-8")
        );


        results.andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("send_message",
                        requestFields(
                                fieldWithPath("postId").type(JsonFieldType.NUMBER).description("게시물 ID"),
                                fieldWithPath("receiverId").type(JsonFieldType.NUMBER).description("수신자 ID"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("내용")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("쪽지 식별자")
                        )
                ));
    }

    @Test
    @DisplayName("쪽지 상세조회")
    public void getMessage() throws Exception {

        ChatResponse.Info response = ChatResponse.Info.build(message,"보내는이","받는이");
        given(chatService.getOne(any(), any())).willReturn(response);

        ResultActions results = mvc.perform(RestDocumentationRequestBuilders
                .get("/api/message/{messageId}",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"));

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("getOne_message", pathParameters(
                                parameterWithName("messageId").description("쪽지 식별자")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("유저 식별자"),
                                fieldWithPath("postId").type(JsonFieldType.NUMBER).description("게시글 식별자"),
                                fieldWithPath("postTitle").type(JsonFieldType.STRING).description("게시글 제목"),
                                fieldWithPath("sender").type(JsonFieldType.STRING).description("보낸이 닉네임"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("내용"),
                                fieldWithPath("createAt").type(JsonFieldType.STRING).description("보낸시간").optional()

                        )
                ));
    }


}
