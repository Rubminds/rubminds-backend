package com.rubminds.api.skill.dto;

import com.rubminds.api.skill.domain.CostomSkill;
import com.rubminds.api.skill.domain.PostSkill;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

public class CostomSkillResponse {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetCostomSkill {
        private Long costomSkillId;
        private String name;

        public static CostomSkillResponse.GetCostomSkill build(CostomSkill costomSkill){
            return CostomSkillResponse.GetCostomSkill.builder()
                    .costomSkillId(costomSkill.getId())
                    .name(costomSkill.getName())
                    .build();
        }
    }


    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class GetCostomSkills {
        private List<CostomSkillResponse.GetCostomSkill> skills;
        public static CostomSkillResponse.GetCostomSkills build(List<CostomSkill> costomSkills){
            return CostomSkillResponse.GetCostomSkills.builder()
                    .skills(costomSkills.stream().map(CostomSkillResponse.GetCostomSkill::build).collect(Collectors.toList()))
                    .build();
        }
    }
}

