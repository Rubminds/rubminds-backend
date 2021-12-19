package com.rubminds.api.skill.domain.repository;


import com.rubminds.api.skill.domain.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    Optional<Skill> findById(Long id);

    @Query("select s from Skill s where s.id in :skillIds")
    List<Skill> findAllByIdIn(@Param("skillIds") List<Long> skillIds);

    @Query("select s from PostSkill ps join ps.post p join ps.skill s where p.id=:postId")
    List<Skill> findAllByPost(Long postId);
}
