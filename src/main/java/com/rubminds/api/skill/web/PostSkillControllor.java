package com.rubminds.api.skill.web;


import com.rubminds.api.skill.domain.PostSkill;
import com.rubminds.api.skill.dto.PostSkillRequest;
import com.rubminds.api.skill.dto.PostSkillResponse;
import com.rubminds.api.skill.service.PostSkillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/postskill")
public class PostSkillControllor {
    private final PostSkillService postSkillService;

    @PostMapping
    public ResponseEntity<PostSkillResponse.addSkill> savePost(@RequestBody PostSkillRequest request) {
        PostSkillResponse.addSkill response =  postSkillService.create(request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{postskillid}")
    public void delete(@PathVariable("postskillid") Long postId) {
        postSkillService.delete(postId);
    }

    @GetMapping("/{post_id}")
    public ResponseEntity<List<PostSkillResponse.addSkill>> findskill (@PathVariable("post_id") Long postId) {
        List<PostSkill> skills = postSkillService.findAll(postId);

        List<PostSkillResponse.addSkill> collect = skills.stream()
                .map(s -> new PostSkillResponse.addSkill(s.getId(),s.getSkill().getName(),s.getPost().getId()))
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(collect);

    }
}
