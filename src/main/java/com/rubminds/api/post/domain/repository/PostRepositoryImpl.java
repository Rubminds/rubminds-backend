package com.rubminds.api.post.domain.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.rubminds.api.post.domain.Kinds;
import com.rubminds.api.post.domain.Post;
import com.rubminds.api.post.domain.PostStatus;
import com.rubminds.api.user.domain.User;
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
import static com.rubminds.api.post.domain.QPostLike.postLike;
import static com.rubminds.api.post.domain.QPostSkill.postSkill;
import static com.rubminds.api.skill.domain.QCustomSkill.customSkill;
import static com.rubminds.api.team.domain.QTeam.team;
import static com.rubminds.api.team.domain.QTeamUser.teamUser;
import static com.rubminds.api.user.domain.QUser.user;


@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Post> findByIdWithSkillAndUser(Long postId) {
        return Optional.ofNullable(queryFactory.selectFrom(post)
                .leftJoin(post.customSkills).fetchJoin()
                .leftJoin(post.postLikeList).fetchJoin()
                .leftJoin(post.postFileList).fetchJoin()
                .join(post.writer, user).fetchJoin()
                .leftJoin(user.avatar).fetchJoin()
                .where(post.id.eq(postId))
                .fetchOne());
    }

    @Override
    public Page<Post> findAllByKindsAndStatus(Kinds kinds, PostStatus postStatus, String region, List<Long> skillId, List<String> customSkillNameList, Pageable pageable) {
        QueryResults<Post> result = queryFactory.select(post).distinct()
                .from(post)
                .leftJoin(post.postSkills, postSkill)
                .leftJoin(post.customSkills, customSkill)
                .where(postKindsEq(kinds))
                .where(postStatusEq(postStatus))
                .where(postRegionEq(region))
                .where(postSkillEq(skillId, customSkillNameList))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.id.desc())
                .fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }

    @Override
    public Page<Post> findAllLikePostByUserId(Kinds kinds, User user, Pageable pageable) {
        QueryResults<Post> result = queryFactory.selectFrom(post)
                .join(post.postLikeList, postLike)
                .where(postLike.user.id.eq(user.getId())
                        .and(postKindsEq(kinds)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.id.desc())
                .fetchResults();

        return new PageImpl<>(result.getResults(), pageable, result.getTotal());
    }


    private BooleanBuilder postSkillEq(List<Long> skillIdList, List<String> customSkillList) {
        BooleanBuilder expression = new BooleanBuilder();
        if (Objects.nonNull(skillIdList) && !skillIdList.isEmpty()) {
            for (Long skillId : skillIdList) {
                expression.or(postSkill.skill.id.eq(skillId));
            }
        }
        if (Objects.nonNull(customSkillList) && !customSkillList.isEmpty()) {
            for (String skill : customSkillList) {
                expression.or(customSkill.name.eq(skill));
            }
        }
        return expression;
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

    private BooleanExpression postRegionEq(String region) {
        if (Objects.nonNull(region)) {
            return post.region.eq(region);
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
