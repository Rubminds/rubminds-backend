package com.rubminds.api.team.web;

import com.rubminds.MvcTest;
import com.rubminds.api.post.domain.*;
import com.rubminds.api.skill.domain.CustomSkill;
import com.rubminds.api.skill.domain.Skill;
import com.rubminds.api.team.Service.TeamService;
import com.rubminds.api.team.Service.TeamUserService;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("TeamUser API 문서화")
@WebMvcTest(TeamUserController.class)
public class TeamUserControllerTest extends MvcTest {

    @MockBean
    private TeamUserService teamUserService;

    @MockBean
    private TeamService teamService;

    private User user1;
    private User user2;
    private TeamUser user3;
    private Post post1;
    private Team team;
    private List<TeamUser> userList = new ArrayList<>();

    @BeforeEach
    public void setup() {
        user1 = User.builder()
                .id(1L)
                .oauthId("1")
                .nickname("동그라미")
                .job("학생")
                .introduce("안녕하세요!")
                .provider(SignupProvider.RUBMINDS)
                .signupCheck(true)
                .build();

        user2 = User.builder()
                .id(2L)
                .oauthId("2")
                .nickname("동그라미")
                .job("학생")
                .introduce("안녕하세요!")
                .provider(SignupProvider.RUBMINDS)
                .signupCheck(true)
                .build();

        team = Team.builder()
                .id(1L)
                .admin(user1)
                .teamUsers(userList)
                .build();

        user3 = TeamUser.builder()
                .id(1L)
                .team(team)
                .finish(false)
                .user(user2)
                .build();

        userList.add(user3);


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
    @DisplayName("팀원 추가 문서화")
    public void getTeamUser() throws Exception {
        TeamUserRequest.CreateAndDelete request = TeamUserRequest.CreateAndDelete.builder().team_id(1L).build();

        TeamUserResponse.OnlyId response = TeamUserResponse.OnlyId.build(user3);
        given(teamUserService.addTeamUser(any(),any(),any())).willReturn(response);

        ResultActions results = mvc.perform(RestDocumentationRequestBuilders
                .post("/api/team-user/{userId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)

                        .content(objectMapper.writeValueAsString(request))
                        .characterEncoding("UTF-8"));


        results.andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("teamUser_add", pathParameters(
                              parameterWithName("userId").description("유저 식별자")
                        ),

                        requestFields(
                                fieldWithPath("team_id").type(JsonFieldType.NUMBER).description("팀 식별자")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("팀원 식별자")

                        )
                ));

    }

    @Test
    @DisplayName("끝내기 확인 문서화")
    public void changeFinishUser() throws Exception {
        TeamUserResponse.OnlyId response = TeamUserResponse.OnlyId.build(user3);
        given(teamUserService.changeFinish(any(),any())).willReturn(response);

        ResultActions results = mvc.perform(RestDocumentationRequestBuilders.put("/api/team-user/{teamUserId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"));

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("teamUser_finish", pathParameters(
                        parameterWithName("teamUserId").description("팀원식별자")
                        ),
                        responseFields(
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("팀원 식별자")

                        )
                ));
    }

    @Test
    @DisplayName("팀원 삭제 문서화")
    public void deleteTeamUser() throws Exception {
        TeamUserRequest.CreateAndDelete request = TeamUserRequest.CreateAndDelete.builder().team_id(1L).build();
        given(teamUserService.deleteUser(any(),any(),any())).willReturn(1l);

        ResultActions results = mvc.perform(RestDocumentationRequestBuilders.delete("/api/team-user/{teamUserId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .characterEncoding("UTF-8"));

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("teamUser_delete", pathParameters(
                        parameterWithName("teamUserId").description("팀원식별자")
                        ),
                        requestFields(
                                fieldWithPath("team_id").type(JsonFieldType.NUMBER).description("팀 식별자")
                        )
                ));
    }
}
