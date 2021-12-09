package com.rubminds.api.file.domain.repository;

import com.rubminds.api.file.domain.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {
}
