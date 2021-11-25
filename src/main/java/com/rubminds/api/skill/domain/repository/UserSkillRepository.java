package com.rubminds.api.skill.domain.repository;

import com.rubminds.api.skill.domain.UserSkill;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserSkillRepository extends JpaRepository<UserSkill, Long> {
}
