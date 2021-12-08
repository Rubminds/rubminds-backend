package com.rubminds.api.skill.domain.repository;


import com.rubminds.api.post.domain.Post;
import com.rubminds.api.skill.domain.PostSkill;
import com.rubminds.api.skill.domain.Skill;
import com.rubminds.api.skill.dto.PostSkillResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostSkillRepository extends JpaRepository<PostSkill, Long> {
    Optional<PostSkill> findById(Long id);
    List<PostSkill> findAllByPost(Post post);
    List<PostSkill> deleteAllByPost(Post post);
    List<PostSkill> deleteAllByPostId(Long postId);

}