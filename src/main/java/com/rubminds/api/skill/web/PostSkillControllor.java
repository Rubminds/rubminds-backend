package com.rubminds.api.skill.web;

import com.rubminds.api.post.domain.Post;
import com.rubminds.api.skill.Service.PostSkillService;
import com.rubminds.api.skill.domain.Skill;
import com.rubminds.api.skill.dto.PostSkillRequest;
import com.rubminds.api.skill.dto.PostSkillResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
