package com.rubminds.api.skill.service;

import com.rubminds.api.skill.domain.Skill;
import com.rubminds.api.skill.domain.repository.SkillRepository;
import com.rubminds.api.skill.dto.SkillResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SkillService {
    private final SkillRepository skillRepository;

    public SkillResponse.GetSkills getList(){
        List<Skill> skillList = skillRepository.findAll();
        return SkillResponse.GetSkills.build(skillList);
    }

}
