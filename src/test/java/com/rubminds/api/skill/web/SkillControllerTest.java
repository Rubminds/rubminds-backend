package com.rubminds.api.skill.web;

import com.rubminds.MvcTest;
import com.rubminds.api.skill.domain.Skill;
import com.rubminds.api.skill.dto.SkillResponse;
import com.rubminds.api.skill.service.SkillService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("SKILL API 문서화")
@WebMvcTest(SkillController.class)
class SkillControllerTest extends MvcTest {
    @MockBean
    private SkillService skillService;

    private List<Skill> skillList = new ArrayList<>();

    @BeforeEach
    void setup() {
        Skill skill1 = Skill.builder().id(1L).name("SPRING").build();
        Skill skill2 = Skill.builder().id(1L).name("SPRING").build();
        Skill skill3 = Skill.builder().id(1L).name("SPRING").build();
        skillList.addAll(Arrays.asList(skill1, skill2, skill3));
    }

    @Test
    @DisplayName("스킬 목록 조회 문서화")
    public void getList() throws Exception {
        SkillResponse.GetSkills response = SkillResponse.GetSkills.build(skillList);
        given(skillService.getList()).willReturn(response);

        ResultActions results = mvc.perform(get("/api/skills"));

        results.andExpect(status().isOk())
                .andDo(print())
                .andDo(document("skill_list",
                        responseFields(
                                fieldWithPath("skills[].id").type(JsonFieldType.NUMBER).description("스킬 식별자"),
                                fieldWithPath("skills[].name").type(JsonFieldType.STRING).description("스킬 이름")
                        )
                ));
    }

}