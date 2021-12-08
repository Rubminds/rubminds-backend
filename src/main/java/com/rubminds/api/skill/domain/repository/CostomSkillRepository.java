package com.rubminds.api.skill.domain.repository;

import com.rubminds.api.post.domain.Post;
import com.rubminds.api.skill.domain.CostomSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CostomSkillRepository extends JpaRepository<CostomSkill, Long> {
        Optional<CostomSkill> findById(Long id);
        List<CostomSkill> findAllByPost(Post post);
        List<CostomSkill> deleteAllByPost(Post post);
        List<CostomSkill> deleteAllByPostId(Long postId);

        List<CostomSkill> findAllByPostId(Long postId);
}
