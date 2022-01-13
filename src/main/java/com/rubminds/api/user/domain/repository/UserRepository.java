package com.rubminds.api.user.domain.repository;

import com.rubminds.api.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface
UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByNickname(String nickname);

    Optional<User> findByOauthId(String oauthId);

    boolean existsByNickname(String nickname);

    @Query("select u from User u left join fetch u.avatar where u.id=:userId")
    Optional<User> findByIdWithAvatar(@Param("userId") Long userId);

    @Query("select u.nickname from User u where u.id=:userId")
    Optional<String> findNicknameById(Long userId);
}
