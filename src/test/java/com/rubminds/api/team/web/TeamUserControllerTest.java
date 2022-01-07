package com.rubminds.api.team.web;

import com.rubminds.MvcTest;
import com.rubminds.api.file.domain.Avatar;
import com.rubminds.api.file.dto.SavedFile;
import com.rubminds.api.post.domain.*;
import com.rubminds.api.skill.domain.CustomSkill;
import com.rubminds.api.skill.domain.Skill;
import com.rubminds.api.team.service.TeamUserService;
import com.rubminds.api.team.domain.Team;
import com.rubminds.api.team.domain.TeamUser;
import com.rubminds.api.team.dto.TeamUserRequest;
import com.rubminds.api.team.dto.TeamUserResponse;
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
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("TeamUser API 문서화")
@WebMvcTest(TeamUserController.class)
public class TeamUserControllerTest extends MvcTest {

    @MockBean
    private TeamUserService teamUserService;

    private User user1;
    private User user2;
    private User user3;

    private TeamUser teamUser1;
    private TeamUser teamUser2;
    private TeamUser teamUser3;

    private Post post1;

    private Team team;
    private List<TeamUser> teamUserList = new ArrayList<>();

    private Avatar avatar;

    @BeforeEach
    public void setup() {
        avatar = Avatar.create(SavedFile.builder()
                .originalName("white.jpeg")
                .name("cb3ee9d9-f005-46c3-85b8-b6acf630dcb6.jpeg")
                .extension(".jpeg")
                .size(7695L)
                .publicUrl("https://rubminds.s3.ap-northeast-2.amazonaws.com/cb3ee9d9-f005-46c3-85b8-b6acf630dcb6.jpeg")
                .width(225)
                .height(300)
                .build());

        user1 = User.builder()
                .id(1L)
                .oauthId("1")
                .nickname("동그라미")
                .job("학생")
                .introduce("안녕하세요!")
                .provider(SignupProvider.RUBMINDS)
                .signupCheck(true)
                .avatar(avatar)
                .build();

        user2 = User.builder()
                .id(2L)
                .oauthId("2")
                .nickname("네모")
                .job("학생")
                .introduce("안녕하세요!")
                .provider(SignupProvider.RUBMINDS)
                .signupCheck(true)
                .avatar(avatar)
                .build();

        user3 = User.builder()
                .id(3L)
                .oauthId("3")
                .nickname("세모")
                .job("학생")
                .introduce("안녕하세요!")
                .provider(SignupProvider.RUBMINDS)
                .signupCheck(true)
                .avatar(avatar)
                .build();

        team = Team.builder()
                .id(1L)
                .admin(user1)
                .teamUsers(teamUserList)
                .build();

        teamUser1 = TeamUser.builder()
                .id(1L)
                .team(team)
                .finish(false)
                .user(user1)
                .build();

        teamUser2 = TeamUser.builder()
                .id(2L)
                .team(team)
                .finish(false)
                .user(user2)
                .build();

        teamUser3 = TeamUser.builder()
                .id(3L)
                .team(team)
                .finish(false)
                .user(user3)
                .build();

        teamUserList.add(teamUser1);
        teamUserList.add(teamUser2);
        teamUserList.add(teamUser3);

        post1 = Post.builder()
                .id(1L)
                .title("테스트")
                .content("내용")
                .region("서울")
                .postStatus(PostStatus.RECRUIT)
                .kinds(Kinds.PROJECT)
                .headcount(3)
                .meeting(Meeting.BOTH)
                .writer(user1)
                .postSkills(Collections.singletonList(PostSkill.builder().id(1L).skill(Skill.builder().id(1L).name("JAVA").build()).build()))
                .customSkills(Collections.singletonList(CustomSkill.builder().id(1L).name("java").build()))
                .team(Team.builder().id(1L).admin(user1).build())
                .postFileList(Collections.singleton(PostFile.builder().id(1L).url("file url").build()))
                .build();


    }

    @Test
    @DisplayName("팀원 초대 문서화")
    public void getTeamUser() throws Exception {
        TeamUserResponse.OnlyId response = TeamUserResponse.OnlyId.build(teamUser2);
        given(teamUserService.add(any(), any(), any())).willReturn(response);

        ResultActions results = mvc.perform(RestDocumentationRequestBuilders.post("/api/team/{teamId}/user/{userId}", 1L, 1L));

        results.andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("teamUser_add", pathParameters(
                        parameterWithName("teamId").description("팀 식별자"),
                        parameterWithName("userId").description("유저 식별자")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("추가된 유저 식별자")
                        )
                ));

    }

    @Test
    @DisplayName("팀원 목록 문서화")
    public void getTeamUserList() throws Exception {
        List<TeamUserResponse.GetTeamUser> response = new ArrayList<>();
        response.add(TeamUserResponse.GetTeamUser.build(teamUser1));
        response.add(TeamUserResponse.GetTeamUser.build(teamUser2));
        response.add(TeamUserResponse.GetTeamUser.build(teamUser3));

        given(teamUserService.getList(any())).willReturn(response);

        ResultActions results = mvc.perform(RestDocumentationRequestBuilders.get("/api/team/{teamId}/teamUsers", 1L));

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("teamUser_list", pathParameters(
                                parameterWithName("teamId").description("팀 식별자")
                        ),
                        relaxedResponseFields(
                                fieldWithPath("[].userId").type(JsonFieldType.NUMBER).description("유저 식별자"),
                                fieldWithPath("[].userNickname").type(JsonFieldType.STRING).description("유저 닉네임"),
                                fieldWithPath("[].userAvatar").type(JsonFieldType.STRING).description("유저 프로필 이미지"),
                                fieldWithPath("[].admin").type(JsonFieldType.BOOLEAN).description("팀장 여부(팀장이면 true)"),
                                fieldWithPath("[].finish").type(JsonFieldType.BOOLEAN).description("평가 완료 여부(완료하면 true)")
                        )
                ));
    }

    @Test
    @DisplayName("팀원 평가 문서화")
    public void evaluateTeamUser() throws Exception {
        List<TeamUserRequest.EvaluateData> evaluation = new ArrayList<>();
        TeamUserRequest.EvaluateData evaluateData1 = TeamUserRequest.EvaluateData.builder().userId(2L).attendLevel(5).workLevel(3).build();
        TeamUserRequest.EvaluateData evaluateData2 = TeamUserRequest.EvaluateData.builder().userId(3L).attendLevel(2).workLevel(4).build();
        evaluation.add(evaluateData1);
        evaluation.add(evaluateData2);

        TeamUserRequest.Evaluate request = TeamUserRequest.Evaluate.builder().kinds(Kinds.PROJECT).evaluation(evaluation).build();
        TeamUserResponse.OnlyId response = TeamUserResponse.OnlyId.build(teamUser1);
        given(teamUserService.evaluate(any(), any(), any())).willReturn(response);

        ResultActions results = mvc.perform(RestDocumentationRequestBuilders
                .post("/api/team/{teamId}",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .characterEncoding("UTF-8"));

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("teamUser_evaluate", pathParameters(
                                parameterWithName("teamId").description("팀 식별자")
                        ),
                        requestFields(
                                fieldWithPath("kinds").type(JsonFieldType.STRING).description("게시글 Kinds"),
                                fieldWithPath("evaluation").type(JsonFieldType.ARRAY).description("팀원 평가 목록"),
                                fieldWithPath("evaluation[].userId").type(JsonFieldType.NUMBER).description("유저 식별자"),
                                fieldWithPath("evaluation[].attendLevel").type(JsonFieldType.NUMBER).description("참여도"),
                                fieldWithPath("evaluation[].workLevel").type(JsonFieldType.NUMBER).description("숙련도(Kinds.PROJECT의 경우에만 사용)")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("유저 식별자")

                        )
                ));
    }

    @Test
    @DisplayName("팀원 추방 문서화")
    public void deleteTeamUser() throws Exception {
        given(teamUserService.delete(any() ,any(),any())).willReturn(2L);

        ResultActions results = mvc.perform(RestDocumentationRequestBuilders.delete("/api/team/{teamId}/user/{userId}", 1L, 2L));

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("teamUser_delete", pathParameters(
                                parameterWithName("teamId").description("팀 식별자"),
                                parameterWithName("userId").description("유저 식별자")
                        )
                ));
    }
}
