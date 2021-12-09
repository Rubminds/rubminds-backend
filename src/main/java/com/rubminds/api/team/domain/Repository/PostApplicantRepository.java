package com.rubminds.api.team.domain.Repository;

import com.rubminds.api.skill.domain.CostomSkill;
import com.rubminds.api.team.domain.PostApplicant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostApplicantRepository extends JpaRepository<PostApplicant, Long> {

}
