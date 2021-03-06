package com.rubminds.api.post.domain.repository;


import com.rubminds.api.post.domain.Post;
import com.rubminds.api.post.domain.PostSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostSkillRepository extends JpaRepository<PostSkill, Long> {
    Optional<PostSkill> findById(Long id);
    List<PostSkill> deleteAllByPost(Post post);



}