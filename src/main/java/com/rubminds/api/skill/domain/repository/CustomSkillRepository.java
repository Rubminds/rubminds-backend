package com.rubminds.api.skill.domain.repository;

import com.rubminds.api.post.domain.Post;
import com.rubminds.api.skill.domain.CustomSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomSkillRepository extends JpaRepository<CustomSkill, Long> {
    Optional<CustomSkill> findById(Long id);
    List<CustomSkill> deleteAllByPost(Post post);
}
