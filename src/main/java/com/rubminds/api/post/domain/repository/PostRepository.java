package com.rubminds.api.post.domain.repository;

import com.rubminds.api.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    @Query("select count(tu.finish) from TeamUser tu left join Post p on p.team = tu.team where p=:post and tu.finish=true")
    Integer FindCountFinish(@Param("post") Post post);

}

