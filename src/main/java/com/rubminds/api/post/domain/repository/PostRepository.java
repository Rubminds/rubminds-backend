package com.rubminds.api.post.domain.repository;

import com.rubminds.api.post.domain.Post;
import com.rubminds.api.team.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;


public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    @Query("select count(tu.finish) from TeamUser tu left join Post p on p.team = tu.team where p=:post and tu.finish=true")
    Integer findCountFinish(@Param("post") Post post);

    Optional<Post> findByTeam(Team team);
}

