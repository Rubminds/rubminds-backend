package com.rubminds.api.post.domain.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rubminds.api.post.domain.Kinds;
import com.rubminds.api.post.domain.Post;
import com.rubminds.api.post.domain.PostStatus;
import com.rubminds.api.post.domain.QPostLike;
import com.rubminds.api.user.dto.QUserDto_LikeInfo;
import com.rubminds.api.user.dto.QUserDto_ProjectInfo;
import com.rubminds.api.user.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.rubminds.api.post.domain.QPost.post;
import static com.rubminds.api.post.domain.QPostLike.*;
import static com.rubminds.api.team.domain.QTeam.team;
import static com.rubminds.api.team.domain.QTeamUser.teamUser;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Post> findByIdWithSkillAndUser(Long postId) {
        return Optional.ofNullable(queryFactory.selectFrom(post)
                .leftJoin(post.customSkills).fetchJoin()
                .leftJoin(post.postLikeList).fetchJoin()
                .leftJoin(post.postFileList).fetchJoin()
                .join(post.writer).fetchJoin()
                .where(post.id.eq(postId))
                .fetchOne());
    }

    @Override
    public Page<Post> findAllByKindsAndStatus(Kinds kinds, PostStatus postStatus, Pageable pageable) {
        final QueryResults<Post> result = queryFactory.selectFrom(post)
                .where(postKindsEq(kinds))
                .where(postStatusEq(postStatus))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.id.desc())
                .fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    private BooleanExpression postKindsEq(Kinds kinds) {
        if (Objects.nonNull(kinds)) {
            return post.kinds.eq(kinds);
        }
        return null;
    }

    private BooleanExpression postStatusEq(PostStatus postStatus) {
        if (Objects.nonNull(postStatus)) {
            return post.postStatus.eq(postStatus);
        }
        return null;
    }

    public List<UserDto.ProjectInfo> findCountByStatusAndUser(Long userId) {
        return queryFactory.select(new QUserDto_ProjectInfo(post.postStatus.stringValue(), post.count()))
                .from(post)
                .join(post.team, team)
                .join(team.teamUsers, teamUser)
                .where(teamUser.user.id.eq(userId))
                .groupBy(post.postStatus)
                .fetch();
    }

    @Override
    public List<UserDto.LikeInfo> findCountByLikeAndUser(Long userId) {
        return queryFactory.select(new QUserDto_LikeInfo(post.kinds.stringValue(), post.count()))
                .from(post)
                .join(post.postLikeList, postLike).on(postLike.user.id.eq(userId))
                .groupBy(post.kinds)
                .fetch();
    }
}
