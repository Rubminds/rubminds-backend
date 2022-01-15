package com.rubminds.api.post.domain.repository;
import com.rubminds.api.post.domain.Post;
import com.rubminds.api.post.domain.PostFile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PostFileRepository extends JpaRepository<PostFile, Long> {
    List<PostFile> deleteAllByPostAndComplete(Post post, boolean complete);

    PostFile findByPostAndComplete(Post post, boolean complete);

}
