package com.rubminds.api.post.domain.repository;

import com.rubminds.api.post.domain.CompleteFile;
import com.rubminds.api.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompleteFileRepository extends JpaRepository<CompleteFile, Long> {
    List<CompleteFile> deleteAllByPost(Post post);
}
