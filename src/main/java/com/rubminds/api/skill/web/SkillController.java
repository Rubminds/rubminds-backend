package com.rubminds.api.skill.web;

import com.rubminds.api.skill.domain.Skill;
import com.rubminds.api.skill.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/skill")
public class SkillController {
    private final SkillService skillService;

    @GetMapping("/list")
    public List<Skill> getSkillList(){
        return skillService.getSkillList();
    }

}
