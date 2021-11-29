package com.rubminds.api.skill.domain.repository;

import com.rubminds.api.skill.domain.UserSkill;
import com.rubminds.api.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserSkillRepository extends JpaRepository<UserSkill, Long> {
    void deleteAllByUser(User user);
}
