package com.rubminds.api.skill.web;

import com.rubminds.api.skill.dto.SkillResponse;
import com.rubminds.api.skill.service.SkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/skill")
public class SkillController {
    private final SkillService skillService;

    @GetMapping("/list")
    public ResponseEntity<SkillResponse.GetSkills> getSkillList(){
        SkillResponse.GetSkills response = skillService.getSkillList();
        return ResponseEntity.ok().body(response);
    }

}
