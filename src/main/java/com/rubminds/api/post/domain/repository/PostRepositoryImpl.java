package com.rubminds.api.post.domain.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rubminds.api.post.domain.Post;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.rubminds.api.post.domain.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Post> findByIdWithSkillAndUser(Long postId) {
        return Optional.ofNullable(queryFactory.selectFrom(post)
                .leftJoin(post.customSkills).fetchJoin()
                .leftJoin(post.postLikeList).fetchJoin()
                .join(post.writer).fetchJoin()
                .where(post.id.eq(postId))
                .fetchOne());
    }
}
