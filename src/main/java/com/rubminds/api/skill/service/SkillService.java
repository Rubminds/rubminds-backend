package com.rubminds.api.skill.service;

import com.rubminds.api.skill.domain.Skill;
import com.rubminds.api.skill.domain.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SkillService {
    private final SkillRepository skillRepository;

    public List<Skill> getSkillList(){
        return skillRepository.findAll();
    }

}
