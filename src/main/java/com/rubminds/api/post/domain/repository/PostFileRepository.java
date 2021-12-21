package com.rubminds.api.post.domain.repository;

import com.rubminds.api.post.domain.PostFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostFileRepository extends JpaRepository<PostFile, Long> {
}
