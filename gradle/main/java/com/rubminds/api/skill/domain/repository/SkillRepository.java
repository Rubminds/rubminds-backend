package com.rubminds.api.skill.domain.repository;

import com.rubminds.api.skill.domain.PostSkill;
import com.rubminds.api.skill.domain.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkillRepository extends JpaRepository< Skill, Long> {
    Optional<Skill> findById(Long id);

}

