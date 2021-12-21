package com.rubminds.api.user.domain.repository;

import com.rubminds.api.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface
UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByNickname(String nickname);

    Optional<User> findByOauthId(String oauthId);

    boolean existsByNickname(String nickname);

    @Query("select u from User u join fetch u.avatar where u.id=:userId")
    Optional<User> findByIdWithAvatar(Long userId);
}
